<?php
namespace Planrise {
	use Enum;
	use Enum_PlanetType;
	use Enum_pol_index;
	use Vec3D;

	include_once('Enum.php');
	include_once('APC_Const.php');
	include_once('APC_Math.php');
	include_once('APC_Planets.php');
	include_once('APC_Time.php');
	include_once('APC_Spheric.php');
	include_once('APC_VecMat3d.php');
	
	//
	// Types
	//
	class Enum_enEvent extends Enum { 
		const RISING = 0;
		const TRANSIT = 1;
		const SETTING = 2; 
	};
	
	class Enum_enState extends Enum { 
		const NEVER_RISES = 0;
		const OK = 1;
		const CIRCUMPOLAR = 2;
	};
	
	//------------------------------------------------------------------------------
	//
	// GeocPosition: Geocentric equatorial coordinates of the Sun and planets 
	//
	// Input:
	//
	//   Planet    Identifies planet (or Sun)
	//   T         Time in Julian centuries since J2000
	//
	// Output:
	//
	//   RA        Right ascension in [rad]
	//   Dec       Declination in [rad]
	//
	//------------------------------------------------------------------------------
	function GeocPosition ( Enum_PlanetType $Planet, $T, &$RA, &$Dec )
	{
		//
		// Variables
		//
		$r_plan = Vec3D::MatrixVectorProduct(Ecl2EquMatrix($T), Vec3D::SubtractionOfVectors(PertPosition($Planet, $T), PertPosition(new Enum_PlanetType('EARTH'), $T) ));

		$RA  = $r_plan->getByPolIndex(new Enum_pol_index('PHI'));
		$Dec = $r_plan->getByPolIndex(new Enum_pol_index('THETA'));
	}
	
	//------------------------------------------------------------------------------
	// <return>:   Risetime, settime, transittime
	//             -1 - always invisible
	//             -2 - always visible
	//------------------------------------------------------------------------------
	function Planrise($year,$month,$day,$longitude,$latitude,$zone,$planet,&$risetime,&$settime,&$transtime = null){
		//
		// Constants
		//
		$Sid     =  0.9972696;   // Conversion sidereal/solar time 
		$Sin_h0p = -9.890038e-3; // sin(-34'); altitude for planets
		$Sin_h0s = -1.45439e-2;  // sin(-50'); altitude for the Sun
		$MaxIter =  10;          // Max. number of iteration steps

		$Name = array("Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Pluto" );
		
		//
		// Variables
		//
		$iPlanet = new Enum_PlanetType($planet);
		$State = new Enum_enState("NEVER_RISES");
		$Event = new Enum_enEvent("RISING");
		$iEvent = null; 
		$cnt = null;
		$lambda = null;
		$phi = null; 
		$Cphi = null; 
		$Sphi = null;
		$Date = null;
		$T = null;
		$LT = null;
		$D_LT = null;
		$LST_0h = null;
		$LST = null;
		$RA = null;
		$RA_0h = null;
		$RA_24h = null;
		$Dec = null;
		$Dec_0h = null;
		$Dec_24h = null;
		$Sin_h0 = null;
		$SDA = null;
		$Dtau = null;
		
		//calc
		$lambda = $longitude * Rad;
		$phi = $latitude * Rad;
		$zone /= 24;
		$Date = Mjd(Intval($year), $month, $day) - $zone;
		
		$Cphi = cos($phi);
		$Sphi = sin($phi);
		
		// Local sidereal time at 0h local time 
		$LST_0h = GMST($Date) + $lambda;
		
		// Compute geocentr. planetary position at 0h and 24h local time
		$T = ($Date - MJD_J2000) / 36525.0;
		
		GeocPosition($iPlanet, $T              , $RA_0h , $Dec_0h );
		GeocPosition($iPlanet, $T + 1.0/36525.0, $RA_24h, $Dec_24h);
		
		// Generate continuous right ascension values in case of jumps
		// between 0h and 24h                                          
		if ( $RA_0h - $RA_24h > pi ) {
			$RA_24h = $RA_24h + 2.0 * pi;
		}
		if ( $RA_0h - $RA_24h <-pi ) {
			$RA_0h  = $RA_0h  + 2.0 * pi;
		}
		
		// Compute rising, transit and setting times
		$State = new Enum_enState("OK");
		
		for($i = 0; $i < 3; $i++){
			
			switch($i){
			case 0: $Event = new Enum_enEvent("RISING"); break;
			case 1: $Event = new Enum_enEvent("TRANSIT"); break;
			case 2: $Event = new Enum_enEvent("SETTING"); break;
			}
		  
			// Starting value 12h local time 
			$LT  = 12.0;  
			$cnt = 0;
			
			// Iteration
			do {

				// Linear interpolation of planetary position 
				$RA  = $RA_0h  + ($LT/24.0) * ($RA_24h  - $RA_0h );
				$Dec = $Dec_0h + ($LT/24.0) * ($Dec_24h - $Dec_0h);

				// Compute semi-diurnal arc (in rad)
				if ($iPlanet == new Enum_PlanetType('SUN')) { 
					$Sin_h0 = $Sin_h0s;
				} else {
					$Sin_h0 = $Sin_h0p;
				}

				$SDA = ($Sin_h0 - sin($Dec)*$Sphi ) / (cos($Dec)*$Cphi);

				if (abs($SDA) < 1.0) {
					$SDA = acos($SDA); 
					$State = new Enum_enState("OK");
				}
				else {

					// Test for circumpolar motion or invisibility
					if ($phi * $Dec >= 0.0) {
						$State = new Enum_enState("CIRCUMPOLAR");
					} else {
						$State = new Enum_enState("NEVER_RISES");
					}

					break;  // Terminate iteration loop
				}

				// Local sidereal time 
				$LST = $LST_0h + $LT / ($Sid * 12.0 / pi); 

				// Difference in hour angle
				switch ($Event->toInt()) {
				case Enum_enEvent::RISING : $Dtau = ($LST - $RA) + $SDA; break;
				case Enum_enEvent::TRANSIT: $Dtau = ($LST - $RA);       break;
				case Enum_enEvent::SETTING: $Dtau = ($LST - $RA) - $SDA;
				}

				// Improved times for rising, culmination and setting
				$D_LT = $Sid * (12.0 / pi)*(Modulo($Dtau + pi,2 * pi) - pi);
				$LT   = $LT - $D_LT;
				$cnt++;

			}
			while ( (abs($D_LT) > 0.008) && ($cnt <= $MaxIter) );
			
			// Print result 
			switch ($State->toInt()) {
			case Enum_enState::OK          : 
				// round to 1 min
				$Hour = floor(60.0 * $LT + 0.5) / 60.0 + 0.00001;
				DMS($Hour, $D, $M, $S);
				
				switch ($Event->toInt()) {
				case Enum_enEvent::RISING : $risetime = strtotime($year . "-" . $month . "-" . $day . " " . $D . ":" . $M . ":00"); break;
				case Enum_enEvent::TRANSIT: $transtime = strtotime($year . "-" . $month . "-" . $day . " " . $D . ":" . $M . ":00"); break;
				case Enum_enEvent::SETTING: $settime = strtotime($year . "-" . $month . "-" . $day . " " . $D . ":" . $M . ":00"); break;
				}
				break;
			case Enum_enState::NEVER_RISES :
				switch ($Event->toInt()) {
				case Enum_enEvent::RISING : $risetime = null; break;
				case Enum_enEvent::TRANSIT: $transtime = null; break;
				case Enum_enEvent::SETTING: $settime = null; break;
				}
				break;
			case Enum_enState::CIRCUMPOLAR :
				switch ($Event->toInt()) {
				case Enum_enEvent::RISING : $risetime = null; break;
				case Enum_enEvent::TRANSIT: $transtime = null; break;
				case Enum_enEvent::SETTING: $settime = null; break;
				}
				break;
			}
		}
	}
}

?>
