<?php
// moonup variables
$a = 0;
$a0 = 0;
$a1 = 0;
$a2 = 0;
$a5 = 0;
$a7 = 0;
$am = 0;
$b = 0;
$b5 = 0;
$bm = 0;
$c = 0;
$c0 = 0;
$d = 0;
$d0 = 0;
$d1 = 0;
$d2 = 0;
$d5 = 0;
$d7 = 0;
$e = 0;
$f = 0;
$f0 = 0;
$f1 = 0;
$f2 = 0;
$g = 0;
$g1 = 0;
$h0 = 0;
$h1 = 0;
$h2 = 0;
$h3 = 0;
$h7 = 0;
$i = 0;
$j = 0;
$j3 = 0;
$k1 = 0;
$k3 = 0;
$l0 = 0;
$l1 = 0;
$l2 = 0;
$l5 = 0;
$m = 0;
$m8 = 0;
$mr = 0;
$n = 0;
$n0 = 0;
$n1 = 0;
$n7 = 0;
$p = 0;
$p1 = 0;
$p2 = 0;
$r1 = 0;
$r5 = 0;
$s = 0;
$t = 0;
$t0 = 0;
$t1 = 0;
$t2 = 0;
$t3 = 0;
$u = 0;
$v = 0;
$v0 = 0;
$v1 = 0;
$v2 = 0;
$w = 0;
$w8 = 0;
$y = 0;
$z = 0;
$z0 = 0;
$z1 = 0;

$m1 = array(1 => 0, 2 => 0, 3 => 0);
$m2 = array(1 => 0, 2 => 0, 3 => 0);
$m3 = array(1 => 0, 2 => 0, 3 => 0);
	function init_moon_variables()
	{
		global $a, $a0, $a1, $a2, $a5, $a7, $am, $b, $b5, $bm, $c, $c0, $d, $d0, $d1, $d2, $d5, $d7, $e, $f, $f0, $f1, $f2, $g, $g1, $h0, $h1, $h2, $h3, $h7, $i, $j, $j3, $k1, $k3, $l0, $l1, $l2, $l5, $m, $m8, $mr, $n, $n0, $n1, $n7, $p, $p1, $p2, $r1, $r5, $s, $t, $t0, $t1, $t2, $t3, $u, $v, $v0, $v1, $v2, $w, $w8, $y, $z, $z0, $z1, $m1, $m2, $m3;
		// moonup variables
		$a = 0;
		$a0 = 0;
		$a1 = 0;
		$a2 = 0;
		$a5 = 0;
		$a7 = 0;
		$am = 0;
		$b = 0;
		$b5 = 0;
		$bm = 0;
		$c = 0;
		$c0 = 0;
		$d = 0;
		$d0 = 0;
		$d1 = 0;
		$d2 = 0;
		$d5 = 0;
		$d7 = 0;
		$e = 0;
		$f = 0;
		$f0 = 0;
		$f1 = 0;
		$f2 = 0;
		$g = 0;
		$g1 = 0;
		$h0 = 0;
		$h1 = 0;
		$h2 = 0;
		$h3 = 0;
		$h7 = 0;
		$i = 0;
		$j = 0;
		$j3 = 0;
		$k1 = 0;
		$k3 = 0;
		$l0 = 0;
		$l1 = 0;
		$l2 = 0;
		$l5 = 0;
		$m = 0;
		$m8 = 0;
		$mr = 0;
		$n = 0;
		$n0 = 0;
		$n1 = 0;
		$n7 = 0;
		$p = 0;
		$p1 = 0;
		$p2 = 0;
		$r1 = 0;
		$r5 = 0;
		$s = 0;
		$t = 0;
		$t0 = 0;
		$t1 = 0;
		$t2 = 0;
		$t3 = 0;
		$u = 0;
		$v = 0;
		$v0 = 0;
		$v1 = 0;
		$v2 = 0;
		$w = 0;
		$w8 = 0;
		$y = 0;
		$z = 0;
		$z0 = 0;
		$z1 = 0;

		$m1 = array(1 => 0, 2 => 0, 3 => 0);
		$m2 = array(1 => 0, 2 => 0, 3 => 0);
		$m3 = array(1 => 0, 2 => 0, 3 => 0);
	}
	function dtor($num) 
	{
		return $num / 57.29577951;
	}
	
	function rtod($num) 
	{
		return $num * 57.29577951;
	}
	
	function dsin($num) 
	{
		return sin(dtor($num));
	}
	
	function dcos($num) 
	{
		return cos(dtor($num));
	}
	
	function dacs($num) 
	{
		if ($num == 1) 
			$y = 0;
		else
			$y = 1.570796327 - atan($num / sqrt(-1 * $num * $num + 1));
		return rtod($y);
	}
	
	function dasn($num) 
	{
		if ($num == 1)
			$y = 1.570796327;
		else
			$y = atan($num / sqrt(-1 * $num * $num + 1));
		return rtod($y);
	}
	
	function dtan($num) 
	{
		return tan(dtor($num));
	}
	
	function datan($num) 
	{
		return rtod(atan($num));
	}
	
	function datan2($y, $x) 
	{
		return rtod(atan2($y,$x));
	}
	
	function rev($x) 
	{
		return  $x - floor($x / 360.0) * 360.0;
	}
	
	function sgn($num) 
	{
		if ($num < 0) return -1;
		if ($num > 0) return 1;
		return 0;
	}
	
	function intr($num) 
	{
		$n = floor(abs($num)); 
		if ($num < 0) $n = $n * -1;
		return $n;
	}
	
	function _dms($a) 
	{
		$sign = 1;
		if ($a < 0) 
		{
			$a = -1 * $a;
			$sign = -1;
		}
		$a1 = intr($a);
		$mm = ($a - $a1) * 60; 
		$mm = round($mm,6);
		$a2 = intr($mm);
		$ss = ($mm - $a2) * 60; 
		$ss = round($ss,6);
		$a3 = intr($ss);
		return $sign * ($a1 + $a2 / 100 + $a3 / 10000);
	}

	function DMS_($x, $sf) 
	{
		$temp = _dms(abs($x));
		$hr = intr($temp); 
		$temp = ($temp - $hr) * 100; 
		$temp = round($temp,6);
		$mn = intr($temp); 
		$temp = ($temp - $mn) * 100; 
		$temp = round($temp,6);
		if ($mn < 10) 
			$mn = "0" . $mn;
		$sc = intr($temp); 
		if ($sc < 10) 
			$sc = "0" . $sc;
		$tmp = (sgn($x)==-1) ? "-" : "";
		$tmp2 = ($sf == true) ? "&#034;" : '"';
		return $tmp . $hr . "°" . $mn . "'" . $sc . $tmp2;
	}

	function DMST($x) 
	{
		$temp = _dms(abs($x));
		$hr = intr($temp); 
		$temp = ($temp - $hr) * 100; 
		$temp = round($temp,6);
		$mn = intr($temp); 
		$temp = ($temp - $mn) * 100; 
		$temp = round($temp,6);
		if ($mn < 10) 
			$mn = "0" . $mn;
		$sc = intr($temp); 
		if ($sc < 10) 
			$sc = "0" . $sc;
		$tmp = (sgn($x)==-1) ? "-" : "";
		return $tmp . $hr . ":" . $mn . ":" . $sc;
	}

	function julian($year, $month, $day, $hour, $minute, $second, $zone) 
	{
		$ut = $hour + $minute / 60 + $second / 3600;
		$ut = $ut + $zone;

		$y = $year;
		$m = $month;
		$d = $day + $ut / 24;

		if ($m <= 2) 
		{
			$m = $m + 12; 
			$y = $y - 1;
		}
		$A = intr($y / 100);
		$B = 2 - $A + intr($A / 4); 
		if ($y < 1582) $B = 0;
		if ($y < 0) 
		{
			$C = intr((365.25 * $y) - .75);
		} 
		else 
		{
			$C = intr(365.25 * $y);
		}
		$D = intr(30.6001 * ($m + 1));
		return round($B + $C + $D + $d + 1720994.5, 6);
	}
//
//    This function adjusts object for perbutations
//
	function perturbations($desc, &$px, &$py, &$pz, &$mlon, &$mlat, $M, $N, $w, $Ls, $Ms) 
	{		
		$x = $px; 
		$y = $py; 
		$z = $pz;

		$lonecl = rev(datan2($y, $x));
		$latecl = datan2($z, sqrt($x * $x + $y * $y));
		$r = sqrt($x * $x + $y * $y + $z * $z);

		if ($desc == "Moon") 
		{
			$Mm = rev($M);
			$Lm = rev($N + $w + $M);
			$D = $Lm - $Ls;
			$F = $Lm - $N;
			$lonecl = $lonecl - 1.274 * dsin($Mm - 2 * $D);
			$lonecl = $lonecl + 0.658 * dsin(2 * $D);
			$lonecl = $lonecl - 0.186 * dsin($Ms);
			$lonecl = $lonecl - 0.059 * dsin(2 * $Mm - 2 * $D);
			$lonecl = $lonecl - 0.057 * dsin($Mm - 2 * $D + $Ms);
			$lonecl = $lonecl + 0.053 * dsin($Mm + 2 * $D);
			$lonecl = $lonecl + 0.046 * dsin(2 * $D - $Ms);
			$lonecl = $lonecl + 0.041 * dsin($Mm - $Ms);
			$lonecl = $lonecl - 0.035 * dsin($D);
			$lonecl = $lonecl - 0.031 * dsin($Mm + $Ms);
			$lonecl = $lonecl - 0.015 * dsin(2 * $F - 2 * $D);
			$lonecl = $lonecl + 0.011 * dsin($Mm - 4 * $D);

			$latecl = $latecl - 0.173 * dsin($F - 2 * $D);
			$latecl = $latecl - 0.055 * dsin($Mm - $F - 2 * $D);
			$latecl = $latecl - 0.046 * dsin($Mm + $F - 2 * $D);
			$latecl = $latecl + 0.033 * dsin($F + 2 * $D);
			$latecl = $latecl + 0.017 * dsin(2 * $Mm + $F);

			$r = $r - 0.58 * dcos($Mm - 2 * $D);
			$r = $r - 0.46 * dcos(2 * $D);

			// needed for phase
			$mlon = $lonecl;
			$mlat = $latecl;
		}
		$px = $r * dcos($lonecl) * dcos($latecl);
		$py = $r * dsin($lonecl) * dcos($latecl);
		$pz = $r * dsin($latecl);
	}
//
//  This converts geocentric coordinates to topocentric coordinates.
//
	function topocentric($desc, $r, &$pRA, &$pDecl, $latitude, $LST) 
	{		
		$RA = $pRA;
		$Decl = $pDecl;
		$mpar = dasn(1/$r); 
		if ($desc != "Moon") 
			$mpar = (8.794 / 3600) / $r;
		$gclat = $latitude - 0.1924 * dsin(2 * $latitude);
		$rho = 0.99883 + 0.00167 * dcos(2 * $latitude);
		$HA = ($LST - $RA) * 15;
		$g = datan(dtan($gclat) / dcos($HA));
		$pRA = $RA * 15 - $mpar * $rho * dcos($gclat) * dsin($HA) / dcos($Decl);
		$pRA = $pRA / 15;
		while ($pRA > 24) 
			$pRA = $pRA - 24;
		while ($pRA <  0) 
			$pRA = $pRA + 24;
		$pDecl = $Decl - $mpar * $rho * dsin($gclat) * dsin($g - $Decl) / dsin($g);
	}
	
	function planet($desc, $latitude, $slon, $LST, $M, $N, $w, $Rs, $Ls, $Ms) 
	{
		global $e;
		global $d;
		global $a;
		global $i;
		
		$oblecl = 23.4393 - 3.563E-7 * $d;

		$M = rev($M);

		$EE = $M + (180 / M_PI) * $e * dsin($M) * (1 + $e * dcos($M));

		$E0 = 0; 
		$E1 = $EE;
		while (abs($E0 - $E1) > .005) 
		{
			$E0 = $E1;
			$E1 = $E0 - ($E0 - (180 / M_PI) * $e * dsin($E0) - $M) / (1 - $e * dcos($E0));
		}
		$EE = $E1;

		$x = $a * (dcos($EE) - $e);
		$y = $a * (dsin($EE) * sqrt(1 - $e * $e));

		$r = sqrt($x * $x + $y * $y);
		$v = datan2($y, $x);

		// need for phase
		$r0 = $r;

		$x = $r * ( dcos($N) * dcos($v + $w) - dsin($N) * dsin($v + $w) * dcos($i) );
		$y = $r * ( dsin($N) * dcos($v + $w) + dcos($N) * dsin($v + $w) * dcos($i) );
		$z = $r * dsin($v+$w) * dsin($i);

		$px = $x; 
		$py = $y; 
		$pz = $z; 
		perturbations($desc, $px, $py, $pz, $mlon, $mlat, $M, $N, $w, $Ls, $Ms);  
		$x = $px; 
		$y = $py; 
		$z = $pz;

		if ($desc != "Moon") 
		{
			$x = $x + $xsun; 
			$y = $y + $ysun; 
			$z = $z + $zsun;
		}

		$x0 = $x;
		$y0 = $y * dcos($oblecl) - $z * dsin($oblecl);
		$z0 = $y * dsin($oblecl) + $z * dcos($oblecl);

		$r = sqrt($x0 * $x0 + $y0 * $y0 + $z0 * $z0);

		// need for phase
		$R0 = $r;

		$RA = datan2($y0, $x0) / 15;
		while ($RA > 24) 
			$RA = $RA - 24;
		while ($RA <  0) 
			$RA = $RA + 24;
		$Decl = datan2($z0, sqrt($x0 * $x0 + $y0 * $y0));

		$pRA = $RA; 
		$pDecl = $Decl; 
		topocentric($desc, $r, $pRA, $pDecl, $latitude, $LST); 
		$Decl = $pDecl; 
		$RA = $pRA;

		$HA = ($LST - $RA) * 15;

		$x = dcos($HA) * dcos($Decl);
		$y = dsin($HA) * dcos($Decl);
		$z = dsin($Decl);

		$xhor = $x * dsin($latitude) - $z * dcos($latitude);
		$yhor = $y;
		$zhor = $x * dcos($latitude) + $z * dsin($latitude);

		$az = datan2($yhor, $xhor) + 180;
		$al = dasn($zhor);

		$elong = dacs( ($Rs * $Rs + $R0 * $R0 - $r0 * $r0) / (2 * $Rs * $R0) );
		$FV    = dacs( ($r0 * $r0 + $R0 * $R0 - $Rs * $Rs) / (2 * $r0 * $R0) );

		if ($desc == "Moon") 
		{
			$elong = dacs( dcos($slon - $mlon) * dcos($mlat) );
			$FV    = 180 - $elong;
		}
		$phase = (1 + dcos($FV)) / 2;
		return round($phase * 100, 2);

	}
	
	function moon_hour(&$isMoonrise) 
	{
		global $a, $a0, $a1, $a2, $a5, $a7, $am, $b, $b5, $bm, $c, $c0, $d, $d0, $d1, $d2, $d5, $d7, $e, $f, $f0, $f1, $f2, $g, $g1, $h0, $h1, $h2, $h3, $h7, $i, $j, $j3, $k1, $k3, $l0, $l1, $l2, $l5, $m, $m8, $mr, $n, $n0, $n1, $n7, $p, $p1, $p2, $r1, $r5, $s, $t, $t0, $t1, $t2, $t3, $u, $v, $v0, $v1, $v2, $w, $w8, $y, $z, $z0, $z1, $m1, $m2, $m3;
		
		$isMoonrise = 0;
		$l0 = $t0 + $c0 * $k1; 
		$l2 = $l0 + $k1;
		if ($a2 < $a0) 
			$a2 = $a2 + 2 * $p1;
		$h0 = $l0 - $a0; 
		$h2 = $l2 - $a2;
		$h1 = ($h2 + $h0) / 2; // hour angle
		$d1 = ($d2 + $d0) / 2; // dec
		if ($c0 <=0) 
			$v0 = $s * sin($d0) + $c * cos($d0) * cos($h0) - $z;
		$v2 = $s * sin($d2) + $c * cos($d2) * cos($h2) - $z;
		if (sgn($v0) == sgn($v2)) return;
		$v1 = $s * sin($d1) + $c * cos($d1) * cos($h1) - $z;
		$a = 2 * $v2 - 4 * $v1 + 2 * $v0; 
		$b = 4 * $v1 - 3 * $v0 - $v2;
		$d = $b * $b - 4 * $a * $v0; 
		if ($d < 0) return;
		$d = sqrt($d);
		if ($v0 < 0 && $v2 > 0) 
		{
			$isMoonrise = 1;
			//$msg = $msg . " Время восхода луны " ; 
			$m8 = 1;
		}
		if ($v0 > 0 && $v2 < 0) 
		{
			$isMoonrise = -1;
			//$msg = $msg . " Заход Луны "; 
			$w8 = 1;
		}
		$e = (-1 * $b + $d) / (2 * $a);
		if ($e > 1 || $e < 0) 
			$e = (-1 * $b - $d) / (2 * $a);
		$t3 = $c0 + $e + 1 / 120;    // round off
		$h3 = intr($t3); 
		$mr = intr(($t3 - $h3) * 60);
	//    am = "am" ; if (h3 >= 12) {h3 = h3 - 12 ; am="pm"}
	//    if (h3 == 0) h3 = 12 // DA fix 03/24/01
		$bm = ""; 
		if ($mr < 10) $bm = "0";
		//echo "<br>Восход Луны=" . $h3 . ":" . $bm . $mr;
		//$msg = $msg + $h3 + ":" + $bm + $mr;

		$h7 = $h0 + $e * ($h2 - $h0);
		$n7 = -1 * cos($d1) * sin($h7);
		$d7 = $c * sin($d1) - $s * cos($d1) * cos($h7);
		$a7 = atan($n7 / $d7) / $r1;
		if ($d7 < 0) $a7 = $a7 + 180;
		if ($a7 < 0) $a7 = $a7 + 360;
		if ($a7 > 360) $a7 = $a7 - 360;

		$a7 = round($a7,2);
		return $h3 . ":" . $bm . $mr;
		//$msg = $msg . ",  az " . $a7;
	}
	
	function moon_flt() 
	{
		global $a, $a0, $a1, $a2, $a5, $a7, $am, $b, $b5, $bm, $c, $c0, $d, $d0, $d1, $d2, $d5, $d7, $e, $f, $f0, $f1, $f2, $g, $g1, $h0, $h1, $h2, $h3, $h7, $i, $j, $j3, $k1, $k3, $l0, $l1, $l2, $l5, $m, $m8, $mr, $n, $n0, $n1, $n7, $p, $p1, $p2, $r1, $r5, $s, $t, $t0, $t1, $t2, $t3, $u, $v, $v0, $v1, $v2, $w, $w8, $y, $z, $z0, $z1, $m1, $m2, $m3;
		
		$a = $f1 - $f0; 
		$b = $f2 - $f1 - $a;
		$f = $f0 + $p * (2 * $a + $b * (2 * $p - 1));
	}
	
	function moon_init() 
	{
		global $a, $a0, $a1, $a2, $a5, $a7, $am, $b, $b5, $bm, $c, $c0, $d, $d0, $d1, $d2, $d5, $d7, $e, $f, $f0, $f1, $f2, $g, $g1, $h0, $h1, $h2, $h3, $h7, $i, $j, $j3, $k1, $k3, $l0, $l1, $l2, $l5, $m, $m8, $mr, $n, $n0, $n1, $n7, $p, $p1, $p2, $r1, $r5, $s, $t, $t0, $t1, $t2, $t3, $u, $v, $v0, $v1, $v2, $w, $w8, $y, $z, $z0, $z1, $m1, $m2, $m3;
		$l = .606434 + .03660110129 * $t;
		$m = .374897 + .03629164709 * $t;
		$f = .259091 + .0367481952 * $t;
		$d = .827362 + .03386319198 * $t;
		$n = .347343 - .00014709391 * $t;
		$g = .993126 + .0027377785 * $t;
		$l = $l - intr($l); 
		$m = $m - intr($m);
		$f = $f - intr($f); 
		$d = $d - intr($d);
		$n = $n - intr($n); 
		$g = $g - intr($g);
		$l = $l * $p2; 
		$m = $m * $p2; 
		$f = $f * $p2;
		$d = $d * $p2;
		$n = $n * $p2; 
		$g = $g * $p2;
		$v = .39558 * sin($f + $n);
		$v = $v + .082 * sin($f);
		$v = $v + .03257 * sin($m - $f - $n);
		$v = $v + .01092 * sin($m + $f + $n);
		$v = $v + .00666 * sin($m - $f);
		$v = $v - .00644 * sin($m + $f - 2 * $d + $n);
		$v = $v - .00331 * sin($f - 2 * $d + $n);
		$v = $v - .00304 * sin($f - 2 * $d);
		$v = $v - .0024 * sin($m - $f - 2 * $d - $n);
		$v = $v + .00226 * sin($m + $f);
		$v = $v - .00108 * sin($m + $f - 2 * $d);
		$v = $v - .00079 * sin($f - $n);
		$v = $v + .00078 * sin($f + 2 * $d + $n);
		$u = 1 - .10828 * cos($m);
		$u = $u - .0188 * cos($m - 2 * $d);
		$u = $u - .01479 * cos(2 * $d);
		$u = $u + .00181 * cos(2 * $m - 2 * $d);
		$u = $u - .00147 * cos(2 * $m);
		$u = $u - .00105 * cos(2 * $d - $g);
		$u = $u - .00075 * cos($m - 2 * $d + $g);
		$w = .10478 * sin($m);
		$w = $w - .04105 * sin(2 * $f + 2 * $n);
		$w = $w - .0213 * sin($m - 2 * $d);
		$w = $w - .01779 * sin(2 * $f + $n);
		$w = $w + .01774 * sin($n);
		$w = $w + .00987 * sin(2 * $d);
		$w = $w - .00338 * sin($m - 2 * $f - 2 * $n);
		$w = $w - .00309 * sin($g);
		$w = $w - .0019 * sin(2 * $f);
		$w = $w - .00144 * sin($m + $n);
		$w = $w - .00144 * sin($m - 2 * $f - $n);
		$w = $w - .00113 * sin($m + 2 * $f + 2 * $n);
		$w = $w - .00094 * sin($m - 2 * $d + $g);
		$w = $w - .00092 * sin(2 * $m - 2 * $d);
		//  Compute ra, dec, dist
		$s = $w / sqrt($u - $v * $v);
		$a5 = $l + atan($s / sqrt(1 - $s * $s));
		$s = $v / sqrt($u); 
		$d5 = atan($s / sqrt(1 - $s * $s));
		$r5 = 60.40974 * sqrt($u);
	}
	
	function moon_lst($t, $z0, $l5, $r1) 
	{
		global $a, $a0, $a1, $a2, $a5, $a7, $am, $b, $b5, $bm, $c, $c0, $d, $d0, $d1, $d2, $d5, $d7, $e, $f, $f0, $f1, $f2, $g, $g1, $h0, $h1, $h2, $h3, $h7, $i, $j, $j3, $k1, $k3, $l0, $l1, $l2, $l5, $m, $m8, $mr, $n, $n0, $n1, $n7, $p, $p1, $p2, $r1, $r5, $s, $t, $t0, $t1, $t2, $t3, $u, $v, $v0, $v1, $v2, $w, $w8, $y, $z, $z0, $z1, $m1, $m2, $m3;
		$t0 = $t / 36525;
		$s = 24110.5 + 8640184.812999999 * $t0;
		$s = $s + 86636.6 * $z0 + 86400 * $l5;
		$s = $s / 86400; 
		$s = $s - intr($s);
		$t0 = $s * 360 * $r1;
	}

	function moon_jd($year, $month, $day) 
	{
		global $a, $a0, $a1, $a2, $a5, $a7, $am, $b, $b5, $bm, $c, $c0, $d, $d0, $d1, $d2, $d5, $d7, $e, $f, $f0, $f1, $f2, $g, $g1, $h0, $h1, $h2, $h3, $h7, $i, $j, $j3, $k1, $k3, $l0, $l1, $l2, $l5, $m, $m8, $mr, $n, $n0, $n1, $n7, $p, $p1, $p2, $r1, $r5, $s, $t, $t0, $t1, $t2, $t3, $u, $v, $v0, $v1, $v2, $w, $w8, $y, $z, $z0, $z1, $m1, $m2, $m3;
		
		$y = $year;
		$m = $month;
		$d = $day;

		$g = 1; 
		if ($y < 1582) $g = 0;
		$d1 = intr($d); 
		$f = $d - $d1 - .5;
		$j = -1 * intr(7 * (intr(($m + 9) / 12) + $y) / 4);
		if ($g != 0) {
			$s = sgn($m - 9); 
			$a = abs($m - 9);
			$j3 = intr($y + $s * intr($a / 7));
			$j3 = -1 * intr((intr($j3 / 100) + 1) * 3 / 4);
		}
		$j = $j + intr(275 * $m / 9) + $d1 + $g * $j3;
		$j = $j + 1721027 + 2 * $g + 367 * $y;
		if ($f < 0) 
		{
			$f = $f + 1; 
			$j = $j - 1;
		}
	}
	
	function moon_up($year, $month, $day, $latitude, $longitude, $zone, &$moonRise, &$moonSet) 
	{
		global $a, $a0, $a1, $a2, $a5, $a7, $am, $b, $b5, $bm, $c, $c0, $d, $d0, $d1, $d2, $d5, $d7, $e, $f, $f0, $f1, $f2, $g, $g1, $h0, $h1, $h2, $h3, $h7, $i, $j, $j3, $k1, $k3, $l0, $l1, $l2, $l5, $m, $m8, $mr, $n, $n0, $n1, $n7, $p, $p1, $p2, $r1, $r5, $s, $t, $t0, $t1, $t2, $t3, $u, $v, $v0, $v1, $v2, $w, $w8, $y, $z, $z0, $z1, $m1, $m2, $m3;
		
		$p1 = 3.14159265;
		
		$p2 = 2 * $p1;
		$r1 = $p1 / 180; 
		$k1 = 15 * $r1 * 1.0027379;
		
		//echo "<br>k1=" . $k1;

		$b5 = $latitude; 
		$l5 = $longitude / 360; 
		$z0 = $zone / 24;
		
		moon_jd($year, $month, $day); 
		
		$t = ($j - 2451545) + $f;
		moon_lst($t, $z0, $l5, $r1); 
		$t = $t + $z0;

		for ($i = 1 ; $i <= 3 ; $i++) 
		{
			moon_init(); 
			$m1[$i] = $a5;
			$m2[$i] = $d5; 
			$m3[$i] = $r5; 
			$t = $t + .5;
		}
		if ($m1[2] <= $m1[1]) $m1[2] = $m1[2] + $p2;
		if ($m1[3] <= $m1[2]) $m1[3] = $m1[3] + $p2;
		$z1 = $r1 * (90.567 - 41.685 / $m3[2]);
		$s = sin($b5 * $r1); 
		$c = cos($b5 * $r1);
		$z = cos($z1); 
		$m8 = 0; 
		$w8 = 0;
		$a0 = $m1[1]; 
		$d0 = $m2[1];
		for ($c0 = 0 ; $c0 <= 23 ; $c0++) 
		{
			$p = ($c0 + 1) / 24;
			$f0 = $m1[1]; 
			$f1 = $m1[2]; 
			$f2 = $m1[3];
			moon_flt(); 
			$a2 = $f;
			$f0 = $m2[1]; 
			$f1 = $m2[2]; 
			$f2 = $m2[3];
			moon_flt(); 
			$d2 = $f;
			$mmm = moon_hour($isMoonrise);
			if ($isMoonrise == 1)
				$moonRise = $mmm;
			if ($isMoonrise == -1)
				$moonSet = $mmm;
			$a0 = $a2; 
			$d0 = $d2; 
			$v0 = $v2;
		}
		// special message routine
		/*if ($m8 != 0 && $w8 != 0) {
		  if (m8 == 0) msg = msg + " No moonrise this date"
		  if (w8 == 0) msg = msg + " No moonset this date"
		}
		else {
		  if (v2 < 0) msg = msg + " Moon down rest of the day"
		  if (v2 > 0) msg = msg + " Moon up rest of the day"
		}*/
	}

	function cMoon_phase($ag)
	{
		//
		$iag = 1;
		if ($ag >=  1 && $ag <=  6) $iag = 2;
		if ($ag ==  7)             $iag = 3;
		if ($ag >=  8 && $ag <= 13) $iag = 4;
		if ($ag >= 14 && $ag <= 15) $iag = 5;
		if ($ag >= 16 && $ag <= 20) $iag = 6;
		if ($ag == 21)             $iag = 7;
		if ($ag >= 22 && $ag <= 28) $iag = 8;
		return $iag;
	}
	function cMoon_long($Ls, $Lm, $Mm, $Ms) 
	{
		$D = $Lm - $Ls;
		$x = 6.3 * dsin($Mm);
		$x = $x + 1.3 * dsin(2 * $D - $Mm);
		$x = $x + 0.7 * dsin(2 * $D);
		$x = $x - 1.9 * dsin($Ms);
		return rev($D + $x);
	}
	
	function cMoon_day($Ls, $Lm, $Mm, $Ms) 
	{
		// Calculate illumination (synodic) phase
		$ag = round(( cMoon_long($Ls, $Lm, $Mm, $Ms) / 360 ) * 29, 5);
		if ($ag <= 0 || $ag >= 29) $ag = 0;
		return $ag;
	}

	function cMoon($Ls, $Lm, $Mm, $Ms, &$moonPhase) 
	{
		$ag = cMoon_day($Ls, $Lm, $Mm, $Ms);
		$moonPhase = cMoon_phase(intval($ag));
		$ah = round(( cMoon_long($Ls, $Lm, $Mm, $Ms) / 360 ) * 29 * 24, 5);
		if ($ah <= 0 || $ah >= 29*24) $ah = 0;
		return $ah;
	}
	
	function getSunriseTime($dt, $lat, $lng, $zone)
	{
		$year = gmdate("Y", $dt);
		$month = gmdate("m", $dt);
		$day = gmdate("d", $dt);
		
		$sunriseTime = null;
	
		$dr = M_PI / 180;
		$rd = 1 / $dr;
		
		$b5 = $lat; 
		$l5 = $lng;
		
		$b5 = $dr * $b5;
		
		$n = intr(275 * $month / 9) - 2 * intr(($month + 9) / 12) + $day - 30;
		$l0 = 4.8771 + .0172 * ($n + .5 - $l5 / 360);
		$c = .03342 * sin($l0 + 1.345);
		$c2 = $rd * (atan(tan($l0 + $c)) - atan(.9175 * tan($l0 + $c)) - $c);
		$sd = .3978 * sin($l0 + $c);
		$cd = sqrt(1 - $sd * $sd);
		$sc = ($sd * sin($b5) + .0145) / (cos($b5) * $cd);

		if ($sc < -1) 
			$sunriseTime = null;
		else
		{
			$c3 = $rd * atan($sc / sqrt(1 - $sc * $sc));
			$r1 = 6 - $zone - ($l5 + $c2 + $c3) / 15;
			$hr = intr($r1);
			$mr = intr(($r1 - $hr)*60);
			$bm = "";
			if ($mr < 10) 
				$bm = "0";
				
			$sunriseTime = strtotime($year . "-" . $month . "-" . $day . " " . $hr . ":" . ($bm + $mr) . ":00");
			
		}
		return $sunriseTime;
	}
	
	function getSunsetTime($dt, $lat, $lng, $zone)
	{
		$year = gmdate("Y", $dt);
		$month = gmdate("m", $dt);
		$day = gmdate("d", $dt);
	
		$sunsetTime = null;
	
		$dr = M_PI / 180;
		$rd = 1 / $dr;
		
		$b5 = $lat; 
		$l5 = $lng;
		
		$b5 = $dr * $b5;
		
		$n = intr(275 * $month / 9) - 2 * intr(($month + 9) / 12) + $day - 30;
		$l0 = 4.8771 + .0172 * ($n + .5 - $l5 / 360);
		$c = .03342 * sin($l0 + 1.345);
		$c2 = $rd * (atan(tan($l0 + $c)) - atan(.9175 * tan($l0 + $c)) - $c);
		$sd = .3978 * sin($l0 + $c);
		$cd = sqrt(1 - $sd * $sd);
		$sc = ($sd * sin($b5) + .0145) / (cos($b5) * $cd);
		if ($sc >  1) 
			$sunsetTime = null;
		else
		{
			$c3 = $rd * atan($sc / sqrt(1 - $sc * $sc));			
			$s1 = 18 - $zone - ($l5 + $c2 - $c3) / 15;
			$hs = intr($s1); 
			$ms = intr(($s1 - $hs) * 60);
			$bm = ""; 
			if ($ms < 10) 
				$bm = "0";
			
			$sunsetTime = strtotime($year . "-" . $month . "-" . $day . " " . $hs . ":" . ($bm + $ms) . ":00");
		}
		
		return $sunsetTime;
	}
	
	function getMoonParams($year, $month, $day, $hour, $minute, $second, $latitude, $longitude, $zone, &$moonVisibility, &$moonOld, &$moonRise, &$moonSet, &$moonPhase)
	{
		global $a, $a0, $a1, $a2, $a5, $a7, $am, $b, $b5, $bm, $c, $c0, $d, $d0, $d1, $d2, $d5, $d7, $e, $f, $f0, $f1, $f2, $g, $g1, $h0, $h1, $h2, $h3, $h7, $i, $j, $j3, $k1, $k3, $l0, $l1, $l2, $l5, $m, $m8, $mr, $n, $n0, $n1, $n7, $p, $p1, $p2, $r1, $r5, $s, $t, $t0, $t1, $t2, $t3, $u, $v, $v0, $v1, $v2, $w, $w8, $y, $z, $z0, $z1, $m1, $m2, $m3;
		
		init_moon_variables();
		
		#UT
		$ut = $hour + $minute / 60 + $second / 3600;
		$ut = $ut + $zone;
		$ut_flag = 0;
		if ($ut > 24) 
		{
			$ut = $ut - 24;
			$ut_flag = 1;
		}
		$UT = round($ut,4);
		
		#JD
		$JD = julian($year, $month, $day, $hour, $minute, $second, $zone);
		$d = $JD - 2451543.5;
		
		// sun
		$w = 282.9404 + 4.70935E-5   * $d;  //  (longitude of perihelion)
		$a = 1.000000;                      //  (mean distance, a.u.)
		$e = 0.016709 - 1.151E-9     * $d;  //  (eccentricity)
		$M = 356.0470 + 0.9856002585 * $d;  //  (mean anomaly)

		$oblecl = 23.4393 - 3.563E-7 * $d;

		$M = rev($M); 
		$L = rev($w + $M);

		// needed for perturbations or moon day calc
		$Ms = $M; 
		$Ls = $L;
		
		$EE = $M + (180 / M_PI) * $e * dsin($M) * (1 + $e * dcos($M));

		$E0 = 0; 
		$E1 = $EE;
		while (abs($E0 - $E1) > .005) 
		{
			$E0 = $E1;
			$E1 = $E0 - ($E0 - (180 / M_PI) * $e * dsin($E0) - $M) / (1 - $e * dcos($E0));
		}
		$EE = $E1;
		
		$x = $a * (dcos($EE) - $e);
		$y = $a * (dsin($EE) * sqrt(1 - $e * $e));

		$r = sqrt($x * $x + $y * $y);
		$v = datan2($y, $x);

		$lng = rev($v + $w);

		$x = $r * dcos($lng);
		$y = $r * dsin($lng);
		$z = 0.0;
		
		// needed for phase angle
		$Rs = $r;
		$slon = rev(datan2($y,$x));
		$slat = datan2($z, sqrt($x * $x + $y * $y));

		$xsun = $x; 
		$ysun = $y; 
		$zsun = $z;

		$GST = rev(($L / 15) + 12) + $UT;
		while ($GST > 24) 
			$GST = $GST - 24;
		while ($GST <  0) 
			$GST = $GST + 24;

		$LST = $GST + $longitude / 15;
		while ($LST > 24) 
			$LST = $LST - 24;
		while ($LST <  0) 
			$LST = $LST + 24;
		
		// sun az/al
		$x0 = $x;
		$y0 = $y * dcos($oblecl) - $z * dsin($oblecl);
		$z0 = $y * dsin($oblecl) + $z * dcos($oblecl);

		$r = sqrt($x0 * $x0 + $y0 * $y0 + $z0 * $z0);
		$RA = datan2($y0,$x0) / 15;
		while ($RA > 24) 
			$RA = $RA - 24;
		while ($RA <  0) 
			$RA = $RA + 24;
		$Decl = datan2($z0, sqrt($x0 * $x0 + $y0 * $y0));
		$HA = ($LST - $RA) * 15;

		$x = dcos($HA) * dcos($Decl);
		$y = dsin($HA) * dcos($Decl);
		$z = dsin($Decl);

		$xhor = $x * dsin($latitude) - $z * dcos($latitude);
		$yhor = $y;
		$zhor = $x * dcos($latitude) + $z * dsin($latitude);

		$az = datan2($yhor, $xhor) + 180;
		$al = dasn($zhor); 
		$sunal = $al;
		
		// Moon
		$N = 125.1228 - 0.0529538083  * $d;
		$i =   5.1454;
		$w = 318.0634 + 0.1643573223  * $d;
		$a =  60.2666;
		$e = 0.054900;
		$M = 115.3654 + 13.0649929509 * $d;
		$Mm = rev($M); 
		$Lm = rev($N + $w + $M); // moon day calc
		
		$moonVisibility = planet("Moon", $latitude, $slon, $LST, $M, $N, $w, $Rs, $Ls, $Ms);
		
		$moonRise_ = null;
		$moonSet_ = null;
		
		moon_up($year, $month, $day, $latitude, $longitude, $zone, $moonRise_, $moonSet_);
		
		$moonRise = strtotime($year . "-" . $month . "-" . $day . " " . $moonRise_ . ":00");
		$moonSet = strtotime($year . "-" . $month . "-" . $day . " " . $moonSet_ . ":00");
		$moonOld = cMoon($Ls, $Lm, $Mm, $Ms, $moonPhase);
	}
	
?>
