<?php
namespace EclTimer {
	use Enum;
	use Enum_index;
	use Enum_pol_index;
	use Polar;
	use Vec3D;
	use Mat3D;
	use Cheb3D;
	
	include_once('Enum.php');
	include_once('APC_Cheb.php');
	include_once('APC_Const.php');
	include_once('APC_Math.php');
	include_once('APC_Moon.php');
	include_once('APC_PrecNut.php');
	include_once('APC_Spheric.php');
	include_once('APC_Sun.php');
	include_once('APC_Time.php');
	include_once('APC_VecMat3d.php');
	
	
	//
	// Constants
	//
	const Degree   = 8;             // Degree of Chebyshev polynomials
	const Interval = 5.4757015742642026009582477754962e-5;   // Interval for Chebyshev polyn. [cy]  

	const k_Moon   = 0.2725076;     // Ratio Moon/Earth radii (penumbral)
	const k_Sun    = 109.12277362496290060875142694489; // Ratio Sun/Earth radii
	
	//
	// Types
	//
	class Enum_enPhase extends Enum {
		const NOECLIPSE = 0;
		const PARTIAL = 1;
		const ANNULAR = 2;
		const TOTAL = 3;
	}
	
	class Enum_enShadow extends Enum { 
		const PENUMBRA = 0;
		const UMBRA = 1;
	}
	
	// Structure holding the local circumstances of an eclipse
	class LocCircs {

		// Times in Julian cent. since J2000:
		public $Times = array();  // Contact times (1st to 4th contact) 
		public $TMax = null;      // Time of maximum eclipse

		public $Mag = null;       // Magnitude of the eclipse
		public $Obsc = null;      // Maximum obscuration
		public $Phase = null;    // Maximal phase
	};
	
	//
	// Global variables and objects
	//
	$ShadowType = new Enum_enShadow("PENUMBRA");

	$ET_UT = 0.0; // Difference between ephemeris time 
						// and universal time in [s]
	
	// Chebyshev approximation of solar and lunar coordinates
	$LunarEph = new Cheb3D(function($T){return MoonEqu($T);}, Degree, Interval);
	$SolarEph = new Cheb3D(function($T){return SunEqu($T);},  Degree, Interval);
	
	$r_Obs_gc = new Vec3D(); // Observer's geocentric position [Earth radii]
	
	//------------------------------------------------------------------------------
	//
	// Bessel: Computes the orientation of the fundamental plane, the 
	//         coordinates of the shadow center and the parameters of the 
	//         shadow cone
	//
	// Input:
	//
	//   T_UT        Time in Julian centuries UT since J2000
	//
	// Output:
	//
	//   IJK         Unit vectors of the fundamental plane (equatorial)
	//   XSh         Coordinates of the shadow center on the 
	//   YSh           fundamental plane in [Earth radii]
	//   F1          Half angle of the penumbra cone in [rad]
	//   L1          Radius of penumbra cone on the fund. plane [Earth radii]
	//   F2          Half angle of the umbra cone [rad]
	//   L2          Radius of umbra cone on the fund. plane [Earth radii]
	//
	// Globals used:
	//
	//   ET_UT       Difference between ephemeris time and universal time in [s]
	//   SolarEph    Chebyshev polynomials of solar coordinates
	//   LunarEph    Chebyshev polynomials of lunar coordinates
	//
	//------------------------------------------------------------------------------
	function Bessel ( $T_UT, &$IJK, &$XSh, &$YSh, &$F1, &$L1, &$F2, &$L2 ) {
		//
		// Constants
		//
		$tau_Sun  = 8.32 / ( 1440.0*36525.0); // 8.32 min  [cy]


		//
		// Variables
		//
		global $ET_UT, $SolarEph, $LunarEph;
		$T_ET = null;
		$DistMS = null;
		$ZSh = null;
		$r_Sun = new Vec3D();
		$r_Moon = new Vec3D();
		$r_MS = new Vec3D();
		$i = new Vec3D();
		$j = new Vec3D();
		$k = new Vec3D();


		$T_ET = $T_UT + $ET_UT/(86400.0*36525.0);

		// Calculate solar and lunar equatorial coordinates in Earth radii
		$asdf = $SolarEph->Value($T_ET-$tau_Sun);
		$r_Sun  = Vec3D::ScalarDivision(Vec3D::ScalarMultiplication($SolarEph->Value($T_ET-$tau_Sun), AU), R_Earth);
		$r_Moon = Vec3D::ScalarDivision($LunarEph->Value($T_ET), R_Earth);

		// The Sun-Moon direction unit vector becomes vector k
		// of the triade defining the fundamental plane
		$r_MS = Vec3D::SubtractionOfVectors($r_Sun, $r_Moon);
		$DistMS = Vec3D::Norm($r_MS);
		$k = Vec3D::ScalarDivision($r_MS, $DistMS);

		// The unit vector i lies in the equatorial
		// plane and is perpendicular to k
		$i = Vec3D::getNewInstance2(new Polar( $k->getByPolIndex(new Enum_pol_index("PHI")) + pi/2.0, 0 ));
		
		// The unit vector j is perpendicular to k and i
		$j = Vec3D::Cross($k, $i);

		$IJK = Mat3D::getNewInstance1($i, $j, $k);

		// Lunar coordinates in the fundamental plane system
		$XSh = Vec3D::Dot($r_Moon, $i);
		$YSh = Vec3D::Dot($r_Moon, $j);
		$ZSh = Vec3D::Dot($r_Moon, $k);

		// Shadow cones
		$F1 = asin ( (k_Sun + k_Moon) / $DistMS );
		$F2 = asin ( (k_Sun - k_Moon) / $DistMS );

		$L1 = $ZSh * (k_Sun + k_Moon) / $DistMS + k_Moon;
		$L2 = $ZSh * (k_Sun - k_Moon) / $DistMS - k_Moon;
	}
	
	//------------------------------------------------------------------------------
	//
	// Observer: Computes the observer's location on the fundamental plane
	//
	// Input:
	//
	//   T_UT        Time in Julian centuries UT since J2000
	//   IJK         Unit vectors of the fundamental plane (equatorial)
	//
	// <return>:     Observer's location on the fundamental plane [Earth radii]
	//
	//
	// Globals used:
	//
	//   r_Obs_gc    Geocentric coordinates of the observer           
	//
	//------------------------------------------------------------------------------
	function Observer ($T_UT, &$IJK) {
		//
		// Variables
		//
		global $r_Obs_gc;
		$r_Obs_eq = new Vec3D();


		// Compute equatorial coordinates of observer
		$r_Obs_eq = Vec3D::MatrixVectorProduct(Mat3D::R_z(-GMST($T_UT*36525.0 + MJD_J2000)), $r_Obs_gc);

		return Vec3D::MatrixVectorProduct(Mat3D::Transp($IJK), $r_Obs_eq); 
	}
	
	//------------------------------------------------------------------------------
	//
	// ShadowDist:
	//
	//   Computes the shadow distance function f(t)=D(t)**2-L(t)**2, where D is  
	//   the distance of the observer from the shadow axis, while L is the       
	//   radius of the shadow cone. For f(t)=0 the observer touches the shadow   
	//   cone (unit Earth radii squared).                                        
	//
	// Input:
	//
	//   T_UT        Time in Julian centuries UT since J2000
	//
	// <return>:     Distance observer - center of shadow in [Earth radii]
	//
	// Globals used:
	//
	//   ShadowType  Umbra or penumbra                                
	//   ET_UT       Difference between ephemeris time and universal time [s]
	//   r_Obs_gc    Geocentric coordinates of the observer           
	//   SolarEph    Chebyshev polynomials of solar coordinates
	//   LunarEph    Chebyshev polynomials of lunar coordinates
	//
	//------------------------------------------------------------------------------
	function ShadowDist ($T_UT)
	{
		//
		// Variables
		//
		global $ShadowType, $ET_UT, $r_Obs_gc, $SolarEph, $LunarEph;
		$IJK = new Mat3D();
		$XSh = null;
		$YSh = null;
		$F1 = null;
		$L1 = null;
		$F2 = null;
		$L2 = null;
		$LL = null;


		// Fundamental plane coordinates of the shadow and the observer
		Bessel ( $T_UT, $IJK, $XSh, $YSh, $F1, $L1, $F2, $L2 );
		$r_Obs = Observer($T_UT, $IJK);

		// Shadow radius at the observer's place (LL<0 for a total eclipse)
		switch ( $ShadowType ) {
		case new Enum_enShadow("PENUMBRA"): $LL = $L1 - $r_Obs->getByIndex(new Enum_index("Z")) * tan($F1); break;
		case new Enum_enShadow("UMBRA"):    $LL = $L2 - $r_Obs->getByIndex(new Enum_index("Z")) * tan($F2);
		}

		return ($XSh-$r_Obs->getByIndex(new Enum_index("X")))*($XSh-$r_Obs->getByIndex(new Enum_index("X")))+($YSh-$r_Obs->getByIndex(new Enum_index("Y")))*($YSh-$r_Obs->getByIndex(new Enum_index("Y")))-$LL*$LL;
	}
	
	//------------------------------------------------------------------------------
	//
	// Contacts:
	//
	//   Determines the phase, magnitude and times of contacts of a solar        
	//   eclipse for a specified location of the observer based on the time      
	//   of new Moon.                                                            
	//
	// Description:                                                              
	//                                                                           
	//   For computing the times of contact the roots of f(t)=D(t)**2-L(t)**2
	//   are determined, where D is the observer's distance from the shadow 
	//   axis, L is the radius of the shadow cone. 
	//   For f(t)=0 the observer lies on the surface of the shadow cone and 
	//   sees the touching rims of Sun and Moon.    
	//                                                                           
	//   Using quadratic interpolation, the times of 1st and 4th contacts are 
	//   first computed, at which the observer touches the penumbra cone. 
	//   Simultaneously the time of maximum eclipse is obtained, at which the
	//   distance between observer and shadow axis attains a minimum.                               
	//                                                                           
	//   If the eclipse is at least a partial one, Contacts computes the 
	//   magnitude of the eclipse and the obscuration of the Sun by the Moon
	//   for the time of maximum eclipse.                                                          
	//                                                                           
	//   In case the observer's distance from the shadow axis and the umbra
	//   cone diameter at the time of maximum eclipse result in a total or
	//   annular eclipse, the times of 2nd and 3rd contact may subsequently be
	//   derived. Since the duration of the total or annular phase never 
	//   exceeds a value of approximately 13 minutes, the 2nd and 3rd contacts
	//   take place in a corresponding interval before or after the maximum. 
	//   Making use of this fact, one may use a method like the regula falsi 
	//   or the Pegasus method, which converge more rapidly than quadratic 
	//   interpolation, for computing the contact times.                                                        
	//
	// Input:
	//
	//   TNewMoon    Approximate time of New Moon
	//
	// <return>:     Struct holding the local circumstances
	//
	// Globals used:
	//
	//   ET_UT       Difference between ephemeris time and universal time [s]
	//   r_Obs_gc    Geocentric coordinates of the observer           
	//   SolarEph    Chebyshev polynomials of solar coordinates
	//   LunarEph    Chebyshev polynomials of lunar coordinates
	//
	//------------------------------------------------------------------------------

	function Contacts($TNewMoon) {
		//
		// Constants
		//
		$Range = 5.0;       // Search interval = +/-(Range+dTab) [h]
		$dTab  = 0.1;       // Step size [h]
		$hCent  = 876600.0; // 24*36525 hours per century
		$dT  = 0.25/$hCent;  // ~15 min. in Julian centuries
		$eps = 1.0E-10;     // Accuracy for contact times ( ~0.3 s )


		//
		// Variables
		//
		global $ShadowType, $ET_UT, $r_Obs_gc, $SolarEph, $LunarEph;
		$LC = new LocCircs();
		$nContactsFound = null;
		$nRoots = null;
		$Success = null;
		$T_UT = null;
		$PDminus = null;
		$PD0 = null;
		$PDplus = null;
		$xe = null;
		$ye = null;
		$Root1 = null;
		$Root2 = null;
		$XSh = null;
		$YSh = null;
		$F1 = null;
		$L1 = null;
		$F2 = null;
		$L2 = null;
		$LL1 = null;
		$LL2 = null;
		$m = null;
		$A = null;
		$B = null;
		$C = null;
		$S = null;
		$IJK = new Mat3D();


		// Prepare for search of outer contacts
		for ($i = 0; $i < 4; $i++) {
			$LC->Times[$i] = $TNewMoon;
		}

		$nContactsFound = 0;
		$nRoots = 0;

		$T_UT = $TNewMoon + (-$Range-$dTab) / $hCent;
		$ShadowType = new Enum_enShadow("PENUMBRA");
		$PDplus = ShadowDist($T_UT);

		$T_UT = $TNewMoon + (-$Range - 2.0 * $dTab) / $hCent;


		do {  // try to find outer contacts

			$T_UT += 2.0 * $dTab / $hCent; // Compute next time step


			// Compute square of the shadow distance at times T_UT-dTab,
			// T_UT and T_UT+dTab and interpolate
			$PDminus = $PDplus;
			$PD0     = ShadowDist ($T_UT);
			$PDplus  = ShadowDist ($T_UT + $dTab / $hCent);
			Quad ($PDminus, $PD0, $PDplus, $xe, $ye, $Root1, $Root2, $nRoots);


			// Store number of contacts found so far and compute contact times
			$nContactsFound += $nRoots;

			switch ($nRoots) {
			case 1:
				if ( $nContactsFound == 1 ) {
					$LC->Times[0] = $T_UT + ($dTab * $Root1) / $hCent; // 1st contact
				} else {
					$LC->Times[3] = $T_UT + ($dTab * $Root1) / $hCent; // 4th contact
				}
				break;

			case 2:
				$LC->Times[0] = $T_UT + ($dTab * $Root1) / $hCent; // 1st contact
				$LC->Times[3] = $T_UT + ($dTab * $Root2) / $hCent; // 4th contact
			break;
			}


			// If the minimum of the shadow distance lies within the present
			// interval, a sufficiently accurate value of the time of maximum
			// eclipse is determined from its position without further iteration.
			if ( (-1.0 < $xe) && ($xe < 1.0) ) {
				$LC->TMax = $T_UT + $dTab * $xe / $hCent;
			}

		} while ( ($TNewMoon + $Range / $hCent >= $T_UT) && ($nContactsFound != 2));


		// Determine type of eclipse
		if ( $nContactsFound == 0 ) {
			$LC->Phase = new Enum_enPhase("NOECLIPSE"); // No eclipse found
		} else {
			$LC->Phase = new Enum_enPhase("PARTIAL");   // Eclipse is at least partial
		}


		//----------------------------------------------------------------------------
		//
		// Degree of obscuration and magnitude of the eclipse
		//
		//----------------------------------------------------------------------------
		if ( $LC->Phase->toInt() > (new Enum_enPhase("NOECLIPSE"))->toInt() ) {

			// Coordinates of shadow and observer on the fundamental plane
			// at maximum eclipse
			Bessel ( $LC->TMax, $IJK, $XSh, $YSh, $F1, $L1, $F2, $L2 );
			$r_Obs = Observer ($LC->TMax, $IJK);

			// Distance between observer and shadow axis
			$m = sqrt ( 
			($r_Obs->getByIndex(new Enum_index("X")) - $XSh) * ($r_Obs->getByIndex(new Enum_index("X")) - $XSh) + 
			($r_Obs->getByIndex(new Enum_index("Y")) - $YSh) * ($r_Obs->getByIndex(new Enum_index("Y")) - $YSh) );

			// Penumbral and umbral radius at the observer's place
			// (L2<0 for a total eclipse!)
			$LL1 = $L1 - $r_Obs->getByIndex(new Enum_index("Z")) * tan($F1);
			$LL2 = $L2 - $r_Obs->getByIndex(new Enum_index("Z")) * tan($F2);

			// Eclipse type
			if ( $m <  $LL2 ) {
				$LC->Phase = new Enum_enPhase("ANNULAR");
			}
			if ( $m < -$LL2 ) {
				$LC->Phase = new Enum_enPhase("TOTAL");
			}

			// Eclipse magnitude
			if ( $LC->Phase == new Enum_enPhase("PARTIAL") ) {
				$LC->Mag = ($LL1 - $m) / ($LL1 + $LL2); // Penumbra region
			} else {
				$LC->Mag = ($LL1 - $LL2) / ($LL1 + $LL2); // Umbra region
			}

			// Compute degree of obscuration
			switch ( $LC->Phase ) {

			case new Enum_enPhase("NOECLIPSE"): 
				$LC->Obsc = 0.0; break;

			case new Enum_enPhase("PARTIAL"):       

				$B = acos( ($LL1 * $LL2 + $m * $m) / ($m * ($LL1 + $LL2)) );
				$C = acos( ($LL1 * $LL1 + $LL2 * $LL2 - 2.0 * $m * $m) / ($LL1 * $LL1 - $LL2 * $LL2) );
				$A = pi - $B - $C;
				$S = ($LL1 - $LL2) / ($LL1 + $LL2);
				$LC->Obsc = ($S * $S * $A + $B - $S * sin($C)) / pi;
				break;

			case new Enum_enPhase("ANNULAR"):

				$S = ($LL1 - $LL2) / ($LL1 + $LL2);
				$LC->Obsc = $S * $S;
				break;

			case new Enum_enPhase("TOTAL"):
				$LC->Obsc = 1.0;
			}

		} // if ( LC.Phase > noEclipse )



		//----------------------------------------------------------------------------
		// Use the Pegasus method to locate the times of 2nd and 3rd contact
		// starting at the time of maximum eclipse. Search interval
		// [LC.TMaximum-DT,LC.TMaximum] for 2nd contact,
		// [LC.TMaximum,LC.TMaximum+DT] for 3rd contact.
		//----------------------------------------------------------------------------
		if ( $LC->Phase->toInt() > (new Enum_enPhase("PARTIAL"))->toInt() ) {

			$ShadowType = new Enum_enShadow("UMBRA");

			Pegasus(function($T){return ShadowDist($T);}, $LC->TMax - $dT, $LC->TMax, $eps, $LC->Times[1], $Success);
			Pegasus(function($T){return ShadowDist($T);}, $LC->TMax, $LC->TMax + $dT, $eps, $LC->Times[2], $Success);
		}

		return $LC;
	}
	
	//------------------------------------------------------------------------------
	//
	// PosAngles:
	//
	//   Computes the position angles w.r.t. North (standard definition) and     
	//   with respect to the local vertical.                                     
	//
	// Input:
	//
	//   TContacts[] Contact times (in Julian cent. since J2000 UT)
	//   Phase       Maximum phase of the eclipse
	//
	// Output:
	//
	//   P[]         Position angle measured from north [rad]
	//   V[]         Position angle with respect to the vertical [rad]
	//
	// Globals used:
	//
	//   ET_UT       Difference between ephemeris time and universal time [s]
	//   r_Obs_gc    Geocentric coordinates of the observer           
	//   SolarEph    Chebyshev polynomials of solar coordinates
	//   LunarEph    Chebyshev polynomials of lunar coordinates
	//
	//------------------------------------------------------------------------------
	function PosAngles ( $TContacts, $Phase, $P, $V ) {
		//
		// Variables
		//
		global $ET_UT, $r_Obs_gc, $SolarEph, $LunarEph;
		$Contact = null;
		$T_UT = null;
		$XSh = null;
		$YSh = null;
		$F1 = null;
		$L1 = null;
		$F2 = null;
		$L2 = null;
		$LL = null;
		$IJK = new Mat3D();


		for ( $Contact = 0; $Contact <= 3; $Contact++ ) {

			if (($Phase->toInt() > (new Enum_enPhase("PARTIAL"))->toInt()) || 
				( ($Phase->toInt() == (new Enum_enPhase("PARTIAL"))->toInt()) && ( ($Contact == 0) || ($Contact == 3))) ) {

				$T_UT = $TContacts[$Contact];

				// Coordinates of shadow and observer on the fundamental plane
				Bessel ( $T_UT, $IJK, $XSh, $YSh, $F1, $L1, $F2, $L2 );
				$r_Obs = Observer ($T_UT, $IJK);

				// Shadow radius at the observer's place
				switch ($Contact) {
				case 0: 
				case 3: $LL = $L1 - $r_Obs->getByIndex(new Enum_index("Z")) * tan($F1); break;
				case 1: 
				case 2: $LL = $L2 - $r_Obs->getByIndex(new Enum_index("Z")) * tan($F2);
				}

				// Position angles w.r.t North and w.r.t the vertical
				$P[$Contact] = Modulo(atan2(($XSh  - $r_Obs->getByIndex(new Enum_index("X")))/$LL, ($YSh - $r_Obs->getByIndex(new Enum_index("Y")))/$LL), pi2);
				$V[$Contact] = Modulo($P[$Contact] - atan2($r_Obs->getByIndex(new Enum_index("X")), $r_Obs->getByIndex(new Enum_index("Y"))), pi2);
			}
			else {
				$P[$Contact] = 0.0;
				$V[$Contact] = 0.0;
			}
		}
	}
	
	function EclTimer($phases, $lng, $ltd, $cityId){
		//
		// Variables
		//
		global $ET_UT, $r_Obs_gc;
		$eclipses = array();
		for($i = 0; $i < count($phases); $i++) {
			if(substr($phases[$i][2],0,1) == "s"){
				$year = intval(date("Y", strtotime($phases[$i][1])));
				$month = intval(date("m", strtotime($phases[$i][1])));
				$day = intval(date("d", strtotime($phases[$i][1])));
				$hour = intval(date("H", strtotime($phases[$i][1])));
				$minute = intval(date("i", strtotime($phases[$i][1])));
				
				$TNewMoon  = ( Mjd($year, $month, $day) + ($hour + $minute/60)/24.0 - MJD_J2000) / 36525.0;
				$ObsLambda = $lng; // Observers geographic longitude; east pos. [rad]
				$ObsPhi    = $ltd; // Observers geographic latitude [rad]
				$P = array();            // Arrays for position angles of...
				$V = array();            // ...contacts (w.r.t. North / Vertical) in [rad]
				
				$ObsLambda *= Rad;
				$ObsPhi *= Rad;
				
				
				// Geocentric coordinates of the observer (in Earth radii)
				$r_Obs_gc = Vec3D::ScalarDivision(Site ($ObsLambda, $ObsPhi), R_Earth);
				
				$LC = Contacts ($TNewMoon);    // Compute contact times...
				
				PosAngles ($LC->Times, $LC->Phase, $P, $V); // ...and position angles
				
				if($LC->Phase->toInt() > (new Enum_enPhase("NOECLIPSE"))->toInt()){
					$MJD_UT = $LC->TMax * 36525.0 + MJD_J2000;
					$MjdRound = floor(86400.0 * $MJD_UT + 0.5) / 86400.0 + 0.000001;
					CalDat2($MjdRound, $y, $m, $d, $h, $min, $s);
					$maximumTime = strtotime($y . "-" . $m . "-" . $d . " " . $h . ":" . $min . ":" . intval($s));
					
					$MJD_UT = $LC->Times[0] * 36525.0 + MJD_J2000;
					$MjdRound = floor(86400.0 * $MJD_UT + 0.5) / 86400.0 + 0.000001;
					CalDat2($MjdRound, $y, $m, $d, $h, $min, $s);
					$contact1 = strtotime($y . "-" . $m . "-" . $d . " " . $h . ":" . $min . ":" . intval($s));
					
					$MJD_UT = $LC->Times[3] * 36525.0 + MJD_J2000;
					$MjdRound = floor(86400.0 * $MJD_UT + 0.5) / 86400.0 + 0.000001;
					CalDat2($MjdRound, $y, $m, $d, $h, $min, $s);
					$contact4 = strtotime($y . "-" . $m . "-" . $d . " " . $h . ":" . $min . ":" . intval($s));
					
					$contact2 = null;
					$contact3 = null;
					if($LC->Phase->toInt() > (new Enum_enPhase("PARTIAL"))->toInt()){
						$MJD_UT = $LC->Times[1] * 36525.0 + MJD_J2000;
						$MjdRound = floor(86400.0 * $MJD_UT + 0.5) / 86400.0 + 0.000001;
						CalDat2($MjdRound, $y, $m, $d, $h, $min, $s);
						$contact2 = strtotime($y . "-" . $m . "-" . $d . " " . $h . ":" . $min . ":" . intval($s));
						
						$MJD_UT = $LC->Times[2] * 36525.0 + MJD_J2000;
						$MjdRound = floor(86400.0 * $MJD_UT + 0.5) / 86400.0 + 0.000001;
						CalDat2($MjdRound, $y, $m, $d, $h, $min, $s);
						$contact3 = strtotime($y . "-" . $m . "-" . $d . " " . $h . ":" . $min . ":" . intval($s));
					}
					
					$eclipse = array("phase" => $LC->Phase->toInt(),
									 "magnitude" => $LC->Mag,
									 "maximum" => date("Y-m-d H:i:s",$maximumTime),
									 "contact1" => date("Y-m-d H:i:s",$contact1),
									 "contact2" => $contact2 == null ? null : date("Y-m-d H:i:s",$contact2),
									 "contact3" => $contact3 == null ? null : date("Y-m-d H:i:s",$contact3),
									 "contact4" => date("Y-m-d H:i:s",$contact4),
									 "cityid" => $cityId);
					$eclipses[] = $eclipse;
				}
			}
		}
		return $eclipses;
	}
}

?>
