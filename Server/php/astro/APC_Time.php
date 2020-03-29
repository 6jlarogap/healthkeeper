<?php

include_once('Enum.php');
include_once('APC_Const.php');
include_once('APC_Math.php');

// Format tag for date and time output (used by DateTime class output operator)
class Enum_TimeFormat extends Enum { 
	const None = 0;   // don't output time (date only)
	const DDd = 1;    // output time as fractional part of a day
	const HHh = 2;    // output time as hours with one decimal place
	const HHMM = 3;   // output time as hours and minutes (rounded to next minute)
	const HHMMSS = 4; // output time as hours, minutes and seconds (rounded to next s)
};

//------------------------------------------------------------------------------
//
// Modulo: calculates x mod y
//
//------------------------------------------------------------------------------

function Modulo ($x, $y)
{
   return $y * Frac($x / $y);
}

//------------------------------------------------------------------------------
//
// Mjd: Modified Julian Date from calendar date and time
//
// Input:
//
//   Year      Calendar date components
//   Month
//   Day
//   Hour      Time components (optional)
//   Min
//   Sec
//
// <return>:   Modified Julian Date
//
//------------------------------------------------------------------------------
function Mjd ($Year, $Month, $Day, $Hour = null, $Min = null, $Sec = null){
	if($Hour == null) $Hour = 0;
	if($Min == null) $Min = 0;
	if($Sec == null) $Sec = 0;
	
	//
	// Variables
	//
	$MjdMidnight = null;
	$FracOfDay = null;
	$b = null;


	if ($Month <= 2) { 
		$Month += 12;
		--$Year;
	}

	if ( (10000 * $Year + 100 * $Month + $Day) <= 15821004 ){
		$b = intval(-2 + (($Year + 4716) / 4) - 1179);     // Julian calendar 
	}
	else{
		$b = intval(($Year / 400) - ($Year / 100) + ($Year / 4));  // Gregorian calendar 
	}

	$MjdMidnight = 365 * $Year - 679004 + $b + intval(30.6001 * ($Month + 1)) + $Day;
	$FracOfDay   = Ddd($Hour, $Min, $Sec) / 24.0; 

	return $MjdMidnight + $FracOfDay;
}

//------------------------------------------------------------------------------
//
// GMST: Greenwich mean sidereal time
//
// Input:
//
//   MJD       Time as Modified Julian Date
//
// <return>:   GMST in [rad]
//
//------------------------------------------------------------------------------
function GMST ($MJD)
{
  //
  // Constants
  //
  $Secs = 86400.0;        // Seconds per day


  //
  // Variables
  //
  $MJD_0 = null;
  $UT = null;
  $T_0 = null;
  $T = null;
  $gmst = null;


  $MJD_0 = floor($MJD);
  $UT    = $Secs * ($MJD - $MJD_0);     // [s]
  $T_0   = ($MJD_0 - 51544.5) / 36525.0; 
  $T     = ($MJD   - 51544.5) / 36525.0; 

  $gmst  = 24110.54841 + 8640184.812866 * $T_0 + 1.0027379093 * $UT 
          + (0.093104 - 6.2e-6 * $T) * $T * $T;      // [sec]

  return  (pi2 / $Secs) * Modulo($gmst, $Secs);   // [Rad]
}

//------------------------------------------------------------------------------
//
// DMS: Finds degrees, minutes and seconds of arc for a given angle 
//
// Input:
//
//   Dd        Angle in degrees in decimal representation
//
// Output:
//
//   D         Angular degrees
//   M         Minutes of arc
//   S         Seconds of arc
//
//------------------------------------------------------------------------------
function DMS ($Dd, &$D, &$M, &$S)
{
	//
	// Variables
	//
	$x = null;


	$x = abs($Dd);   
	$D = intval($x);
	$x = ($x - $D) * 60.0; 
	$M = intval($x);
	$S = ($x - $M) * 60.0;

	if ($Dd < 0.0) { 
		if ($D != 0) {
			$D *= -1;
		} else {
			if ($M != 0) { 
				$M *= -1;
			} else {
				$S *= -1.0; 
			}
		}
	}
}

//------------------------------------------------------------------------------
//
// ETminUT: Difference ET-UT of ephemeris time and universal time
//
// Input:
//
//   T         Time in Julian centuries since J2000
//
// Output:
//
//   DTsec     ET-UT in [s]
//   valid     Flag indicating T in domain of approximation
//
// Notes: The approximation spans the years from 1825 to 2005
//
//------------------------------------------------------------------------------
function ETminUT ($T, &$DTsec, &$valid)
{
	//
	// Variables
	//
	$i = intval(floor($T/0.25));
	$t = $T-$i*0.25;


	if ( ($T<-1.75) || (0.05<$T) ) {
		$valid = false;
		$DTsec = 0.0;
	}
	else {
		$valid = true;
		switch ($i) {
			case -7: $DTsec=10.4+$t*(-80.8+$t*( 413.9+$t*( -572.3))); break; // 1825-
			case -6: $DTsec= 6.6+$t*( 46.3+$t*(-358.4+$t*(   18.8))); break; // 1850-
			case -5: $DTsec=-3.9+$t*(-10.8+$t*(-166.2+$t*(  867.4))); break; // 1875-
			case -4: $DTsec=-2.6+$t*(114.1+$t*( 327.5+$t*(-1467.4))); break; // 1900-
			case -3: $DTsec=24.2+$t*( -6.3+$t*(  -8.2+$t*(  483.4))); break; // 1925-
			case -2: $DTsec=29.3+$t*( 32.5+$t*(  -3.8+$t*(  550.7))); break; // 1950-
			case -1: $DTsec=45.3+$t*(130.5+$t*(-570.5+$t*( 1516.7))); break; // 1975-
			case  0: $t+=0.25;
			$DTsec=45.3+$t*(130.5+$t*(-570.5+$t*( 1516.7)));        // 2000-
		}                                                              // 2005
	}
}

//------------------------------------------------------------------------------
//
// CalDat: Calendar date and time from Modified Julian Date
//
// Input:
//
//   Mjd       Modified Julian Date
//
// Output:
//
//   Year      Calendar date components
//   Month
//   Day
//   Hour      Decimal hours
//
//------------------------------------------------------------------------------
function CalDat1 ($Mjd, &$Year, &$Month, &$Day, &$Hour )
{
	//
	// Variables
	//
	$a = null;
	$b = null;
	$c = null;
	$d = null;
	$e = null;
	$f = null;
	$FracOfDay = null;


	// Convert Julian day number to calendar date
	$a = intval($Mjd+2400001.0);

	if ( $a < 2299161 ) {  // Julian calendar
		$b = 0;
		$c = $a + 1524;
	}
	else {                // Gregorian calendar
		$b = intval(($a-1867216.25)/36524.25);
		$c = $a +  $b - intval($b/4) + 1525;
	}

	$d     = intval(($c-122.1)/365.25);
	$e     = intval(365*$d + $d/4);
	$f     = intval(($c-$e)/30.6001);

	$Day   = $c - $e - intval(30.6001*$f);
	$Month = $f - 1 - 12*intval($f/14);
	$Year  = $d - 4715 - intval((7+$Month)/10);

	$FracOfDay = $Mjd - floor($Mjd);

	$Hour = 24.0*$FracOfDay;
}

//------------------------------------------------------------------------------
//
// CalDat: Calendar date and time from Modified Julian Date
//
// Input:
//
//   Mjd       Modified Julian Date
//
// Output:
//
//   Year      Calendar date components
//   Month
//   Day
//   Hour      Time components
//   Min
//   Sec
//
//------------------------------------------------------------------------------
function CalDat2 ($Mjd, &$Year, &$Month, &$Day, &$Hour, &$Min, &$Sec )
{
  //
  // Variables
  //
  $Hours = null;


  CalDat1 ($Mjd, $Year, $Month, $Day, $Hours); 
  
  DMS ($Hours, $Hour, $Min, $Sec);
}

?>
