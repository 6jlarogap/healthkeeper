<?php

namespace Sunset {
	use Enum;

	include_once('Enum.php');
	include_once('APC_Const.php');
	include_once('APC_Math.php');
	include_once('APC_Sun.php');
	include_once('APC_Moon.php');
	include_once('APC_Time.php');
		
	// Events to search for
	class Enum_enEvent extends Enum { 
		const MOON     = 0;  // indicates moonrise or moonset
		const SUN      = 1;  // indicates sunrise or sunset
		const CIWILTWI = 2;  // indicates Civil twilight
		const NAUTITWI = 3;  // indicates nautical twilight
		const ASTROTWI = 4;  // indicates astronomical twilight
	};

	//------------------------------------------------------------------------------
	//
	// SinAlt: Sine of the altitude of Sun or Moon
	//
	// Input:
	//
	//   Event     Indicates event to find
	//   MJD0      0h at date to investigate (as Modified Julian Date)
	//   Hour      Hour
	//   lambda    Geographic east longitude in [rad]
	//   Cphi      Cosine of geographic latitude
	//   Sphi      Sine of geographic latitude
	//
	// <return>:   Sine of the altitude of Sun or Moon at instant of Event
	//
	//------------------------------------------------------------------------------
	function SinAlt (Enum_enEvent $Event, $MJD0, $Hour, $lambda, $Cphi, $Sphi)
	{
		//
		// Variables
		//  
		$MJD = null;
		$T = null;
		$RA = null;
		$Dec = null;
		$tau = null;
	  

		$MJD = $MJD0 + $Hour / 24.0;         
		$T   = ($MJD - 51544.5) / 36525.0;

		if ( $Event == new enum_enEvent('MOON')) {
			MiniMoon($T, $RA, $Dec);
		} else {
			MiniSun($T, $RA, $Dec);
		}

		$tau = GMST($MJD) + $lambda - $RA;

		return ( $Sphi * sin($Dec) + $Cphi * cos($Dec) * cos($tau) );
	}

	//------------------------------------------------------------------------------
	//
	// FindEvents: Search for rise/set/twilight events of Sun or Moon
	//
	// Input:
	//
	//   Event     Indicates event to search for
	//   MJD0h     0h at desired date as Modified Julian Date
	//   lambda    Geographic east longitude of the observer in [rad]
	//   phi       Geographic latitude of the observer in [rad]
	//
	// Output:
	//
	//   LT_Rise   Local time of rising or beginning of twilight
	//   LT_Set    Local time of setting or end of twilight
	//   rises     Event takes place
	//   sets      Event takes place
	//   above     Sun or Moon is circumpolar
	//
	//------------------------------------------------------------------------------
	function FindEvents (Enum_enEvent $Event, $MJD0h, $lambda, $phi, &$LT_Rise, &$LT_Set, &$rises, &$sets, &$above )
	{
		//
		// Constants
		//
		$sinh0 = array(
			0 => sin(Rad*( +8.0/60.0)), // Moonrise              at h= +8' 
			1 => sin(Rad*(-50.0/60.0)), // Sunrise               at h=-50'
			2 => sin(Rad*(   - 6.0  )), // Civil twilight        at h=-6 deg
			3 => sin(Rad*(   -12.0  )), // Nautical twilight     at h=-12deg
			4 => sin(Rad*(   -18.0  )), // Astronomical twilight at h=-18deg
		);
	  
		$Cphi = cos($phi);
		$Sphi = sin($phi);

		//
		// Variables
		//
		$hour = 1.0;
		$y_minus = null;
		$y_0 = null;
		$y_plus = null;
		$xe = null;
		$ye = null;
		$root1 = null;
		$root2 = null;
		$nRoot = null;
	  
		// Initialize for search
		$y_minus = SinAlt($Event, $MJD0h, $hour - 1.0, $lambda, $Cphi, $Sphi) - $sinh0[$Event->toInt()];

		$above = ($y_minus>0.0);
		$rises = false;
		$sets  = false;


		// loop over search intervals from [0h-2h] to [22h-24h]
		do {

			$y_0    = SinAlt( $Event, $MJD0h, $hour    , $lambda, $Cphi, $Sphi ) - $sinh0[$Event->toInt()];
			$y_plus = SinAlt( $Event, $MJD0h, $hour+1.0, $lambda, $Cphi, $Sphi ) - $sinh0[$Event->toInt()];
	 
			// find parabola through three values y_minus,y_0,y_plus
			Quad ( $y_minus, $y_0, $y_plus, $xe, $ye, $root1, $root2, $nRoot );

			if ( $nRoot == 1 ) {
				if ( $y_minus < 0.0 ) { 
					$LT_Rise = $hour + $root1;  
					$rises = true; 
				} else { 
					$LT_Set = $hour + $root1;
					$sets = true; 
				}
			}                   

			if ( $nRoot == 2 ) {
				if ( $ye < 0.0 ) { 
					$LT_Rise = $hour + $root2;
					$LT_Set = $hour + $root1; 
				} else {
					$LT_Rise = $hour + $root1;
					$LT_Set = $hour+$root2;
				}
				$rises = true;
				$sets = true;
			}          
	  
			$y_minus = $y_plus;     // prepare for next interval
			$hour += 2.0;         

		}
		while ( !( ( $hour == 25.0 ) || ( $rises && $sets ) ) );
	}

	function Sunset($year,$month,$day,$longitude,$latitude,$zone,$event,&$risetime,&$settime){
		$iEvent = new enum_enEvent($event);
		
		//calc
		$lambda = $longitude * Rad;
		$phi = $latitude * Rad;
		$zone /= 24;
		$Date = Mjd(Intval($year), $month, $day) - $zone;
		
		$LT_Rise = null;
		$LT_Set = null;
		$rise = null;
		$sett = null;
		$above = null;
		
		$Hour = null;
		$H = null;
		$M = null;
		$S = null;
		
		FindEvents($iEvent, $Date, $lambda, $phi, $LT_Rise, $LT_Set, $rise, $sett, $above);
		// Output
		if ($rise || $sett)  {
			if($rise) {
				// round to 1 min
				$Hour = floor(60.0 * $LT_Rise + 0.5) / 60.0 + 0.00001;
				DMS($Hour, $D, $M, $S);
				$risetime = strtotime($year . "-" . $month . "-" . $day . " " . $D . ":" . $M . ":00");
			} else {
				$risetime = null;
			}
			if($sett) {
				// round to 1 min
				$Hour = floor(60.0 * $LT_Set + 0.5) / 60.0 + 0.00001;
				DMS($Hour, $D, $M, $S);
				$settime = strtotime($year . "-" . $month . "-" . $day . " " . $D . ":" . $M . ":00");
			} else {
				$settime = null;
			}
		}
	}
}

?>
