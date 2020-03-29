<?php
namespace Planpos {
	use Enum;
	use Enum_PlanetType;
	use Enum_pol_index;
	use Vec3D;
	use Mat3D;
	
	include_once('Enum.php');
	include_once('APC_Const.php');
	include_once('APC_Math.php');
	include_once('APC_Planets.php');
	include_once('APC_Spheric.php');
	include_once('APC_Sun.php');
	include_once('APC_Time.php');
	include_once('APC_VecMat3d.php');
	include_once('APC_PrecNut.php');
	
	function Planpos($year, $month, $day, $Hour, $planetName, &$l, &$b, &$r){
		//
		// Variables
		//
		$Planet = new Enum_PlanetType($planetName);
		//$year = 2015;
		//$month = 3;
		//$day = 9;
		//$Hour = 0.0;
		$MJD = null;
		$T = null;
		$dist = null;
		$fac = null;
		$R_Sun = new Vec3D();
		$r_helioc = new Vec3D();
		$r_geoc = new Vec3D();
		$r_equ = new Vec3D();
		$P = new Mat3D();
		$End = false;
		
		
		$MJD = Mjd($year,$month,$day) + $Hour / 24.0;
		$T   = ( $MJD - MJD_J2000 ) / 36525.0;
		
		//echo "<br>MJD=" . $MJD;
		//echo "<br>T=" . $T;
		
		$R_Sun = SunPos($T);
		
		//echo "<br>";
		//var_dump($R_Sun);
		
		// Heliocentric ecliptic coordinates of the planet; equinox of date
		$r_helioc = PertPosition($Planet, $T);
		
		//echo "<br>";
		//var_dump($r_helioc);
		
		// Geocentric ecliptic coordinates (equinox of date)
		$r_geoc = Vec3D::AdditionOfVectors($r_helioc, $R_Sun);
		
		//echo "<br>";
		//var_dump($r_geoc);
		
		// First-order light-time/aberration correction
		$dist = Vec3D::Norm($r_geoc);
		$fac = $dist / c_light;
		
		//echo "<br>dist=" . $dist;
		//echo "<br>fac=" . $fac;		
		
		$r_geoc = Vec3D::SubtractionOfVectors($r_geoc, Vec3D::ScalarMultiplication(KepVelocity($Planet,$T),$fac)); 
		
		//echo "<br>";
		//var_dump($r_geoc);
		
		$P = PrecMatrix_Ecl($T,T_J2000);
		$r_helioc = Vec3D::MatrixVectorProduct($P, $r_helioc);
		$r_geoc = Vec3D::MatrixVectorProduct($P, $r_geoc);
		$r_equ = Vec3D::MatrixVectorProduct(Ecl2EquMatrix(T_J2000), $r_geoc);
		
		$l = $r_helioc->getByPolIndex(new Enum_pol_index("PHI")) * Deg;
		$b = $r_helioc->getByPolIndex(new Enum_pol_index("THETA")) * Deg;
		$r = $r_helioc->getByPolIndex(new Enum_pol_index("R"));
		
		//echo "<br>l=" . $r_helioc->getByPolIndex(new Enum_pol_index("PHI")) * Deg;
		//echo "<br>b=" . $r_helioc->getByPolIndex(new Enum_pol_index("THETA")) * Deg;
		//echo "<br>r=" . $r_helioc->getByPolIndex(new Enum_pol_index("R"));
	}
}

?>
