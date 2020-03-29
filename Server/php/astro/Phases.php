<?php
namespace Phases {
	use Enum;
	use Enum_pol_index;
	use Vec3D;
	use Mat3D;
	
	include_once('Enum.php');
	include_once('APC_Const.php');
	include_once('APC_Math.php');
	include_once('APC_Moon.php');
	include_once('APC_Sun.php');
	include_once('APC_Time.php');
	include_once('APC_VecMat3d.php');
	
	
	//
	// Types
	//
	class Enum_enLunarPhase extends Enum { 
		const NEWMOON = 0; 
		const FIRSTQUARTER = 1; 
		const FULLMOON = 2;
		const LASTQUARTER = 3;
	}
	
	//
	// Global variables
	//
	$Phase = new Enum_enLunarPhase("NEWMOON");
	
	//------------------------------------------------------------------------------
	//
	// SolarEclipseFlag: Returns a 2 char. string indicating the possibility of 
	//                   a solar eclipse
	//
	// Input:
	//
	//   beta      Ecliptic latitude of the Moon in [rad]
	//
	// <return>:   Eclipse flag
	//
	//------------------------------------------------------------------------------
	function SolarEclipseFlag ($beta){
		//
		// Constants
		//
		$b = abs($beta);


		if ($b>0.027586) return "";   // No solar eclipse possible

		if ($b<0.015223) return "sc";   // Central eclipse certain
		if ($b<0.018209) return "sc?";   // Possible central eclipse
		if ($b<0.024594) return "sp";   // Partial solar eclipse certain

		return "sp?";                   // Possible partial solar eclipse
	}
	
	//------------------------------------------------------------------------------
	//
	// LunarEclipseFlag: Returns a 2 char. string indicating the possibility of
	//                   a lunar eclipse
	//
	// Input:
	//
	//   beta      Ecliptic latitude of the Moon [rad]
	//
	// <return>:   Eclipse flag
	//
	//------------------------------------------------------------------------------
	function LunarEclipseFlag ($beta)
	{
		//
		// Constants
		//
		$b = abs($beta);


		if ($b>0.028134) return "";   // No lunar eclipse possible

		if ($b<0.006351) return "lt";   // Total lunar eclipse certain
		if ($b<0.009376) return "lt?";   // Possible total eclipse
		if ($b<0.015533) return "lp";   // Partial lunar eclipse certain
		if ($b<0.018568) return "lp?";   // Possible partial eclipse
		if ($b<0.025089) return "lP";   // Penumbral lunar eclipse certain

		return "lP?";                   // Possible penumbral lunar eclipse
	}
	
	//------------------------------------------------------------------------------
	//
	// PhasesFunc: Goal function for search of phase events [-pi, pi]
	//
	// Input:
	//
	//   T         Ephemeris Time (Julian centuries since J2000)
	//
	// <return>:   Difference between the longitude of the Moon from the Sun
	//             and the nominal value for a given phase (New Moon 0, First 
	//             Quarter pi/2, etc.) (in [rad])
	//
	// Global:
	//
	//   Phase     Flag for desired lunar phase
	//
	//------------------------------------------------------------------------------
	function PhasesFunc ($T)
	{
		//
		// Constants
		//
		$tau_Sun  = 8.32 / (1440.0*36525.0);    // 8.32 min  [cy]

		//
		// Variables
		//
		global $Phase;
		$LongDiff = MoonPos($T)->getByPolIndex(new Enum_pol_index("PHI")) - SunPos($T-$tau_Sun)->getByPolIndex(new Enum_pol_index("PHI"));

		return  Modulo ( $LongDiff - $Phase->toInt()*pi/2.0 + pi, pi2 ) - pi;
	}
	
	function Phases($Year){
		global $Phase;
		//
		// Constants
		//
		$dT  = 7.0 / 36525.0;           // Step (1 week)
		$Acc = (0.5/1440.0) / 36525.0;  // Desired Accuracy (0.5 min)
		
		//
		// Variables
		//
		$valid   = false; // Do we have an approximate value for ET-UT ?
		$Success = false; // Flag for success of search for phase time
		$iPhase = null;
		$Lunation = null;
		$MjdUT = null;
		$ET_UT = null;
		$T0 = null;
		$T1 = null;
		$TPhase = null;
		$D0 = null;
		$D1 = null;
		$beta = null;
		$ecl = null;
		
		$result = array();
		
		// Assure that we get the year's first New Moon
		// [T0, T1]: Interval bracketing the instant TPhase to be found
		$T0 = ( Mjd($Year-1, 12, 1) - MJD_J2000 ) / 36525.0;
		$T1 = $T0 + $dT;
		$TPhase = $T0;
		
		// Check 13 months
		for ($Lunation = 0; $Lunation <= 13; $Lunation++) {

			// Search for phases
			for ($i = 0; $i <= 3; $i++) {
				
				switch($i){
				case 0: $Phase = new Enum_enLunarPhase("NEWMOON"); break;
				case 1: $Phase = new Enum_enLunarPhase("FIRSTQUARTER"); break;
				case 2: $Phase = new Enum_enLunarPhase("FULLMOON"); break;
				case 3: $Phase = new Enum_enLunarPhase("LASTQUARTER"); break;
				}
				
				$ph = null;
				
				switch($i){
				case 0: $ph = "NEWMOON"; break;
				case 1: $ph = "FIRSTQUARTER"; break;
				case 2: $ph = "FULLMOON"; break;
				case 3: $ph = "LASTQUARTER"; break;
				}
				
				// Bracket desired phase event
				
				$D0 = PhasesFunc($T0);
				$D1 = PhasesFunc($T1);
				
				while ( ($D0*$D1>0.0) || ($D1<$D0) ) {
					$T0=$T1; $D0=$D1; $T1+=$dT; $D1=PhasesFunc($T1);
				}
											
				// Iterate time of phase
				Pegasus ( function ($T){return PhasesFunc($T);}, $T0, $T1, $Acc, $TPhase, $Success );
				
				// Correct for difference of ephemeris time and universal time
				ETminUT ( $TPhase, $ET_UT, $valid );
				$MjdUT = ( $TPhase*36525.0+MJD_J2000) - $ET_UT/86400.0;
				
				// round to 1 min
				$MjdRound = floor(1440.0*$MjdUT+0.5)/1440.0+0.00001;
				CalDat2 ($MjdRound, $y, $m, $d, $h, $min, $s);
				$time = strtotime(round($y) . "-" . round($m) . "-" . round($d) . " " . round($h) . ":" . round($min) . ":00");
				
				// Eclipse check
				$beta = MoonPos($TPhase)->getByPolIndex(new Enum_pol_index("THETA"));
				switch($i){
				case 0: $ecl = SolarEclipseFlag($beta); break;
				case 1: $ecl = " "; break;
				case 2: $ecl = LunarEclipseFlag($beta); break;
				case 3: $ecl = " "; break;
				}
				
				$part = array($ph,date("Y-m-d H:i:s",$time),$ecl,$Phase->toInt());
				
				$result[$Lunation*4 + $i] = $part;
				
				// Move search interval by one week
				$T0 = $TPhase;
				$T1 = $T0 + $dT;
				
			}
		}
		
		return $result;
	}
}

?>
