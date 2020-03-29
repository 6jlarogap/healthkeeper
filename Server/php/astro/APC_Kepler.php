<?php

include_once('APC_Math.php');
include_once('APC_VecMat3D.php');

//------------------------------------------------------------------------------
//
// Ellip: computes position and velocity vectors for elliptic orbits
//
// Input:
//
//   GM       Product of gravitational constant and centre mass [AU^3*d^-2]
//   M        Mean anomaly in [rad]
//   a        Semi-major axis of the orbit in [AU]
//   e        Eccentricity of the orbit (<1)
//
// Output:
//
//   r        Position w.r.t. orbital plane in [AU]
//   v        Velocity w.r.t. orbital plane in [AU/d]
//
//------------------------------------------------------------------------------
function Ellip ($GM, $M, $a, $e, &$r, &$v )
{
	//
	// Variables
	//
	$k = null; 
	$E_ = null;
	$cosE = null;
	$sinE = null;
	$fac = null;
	$rho = null;


	$k  = sqrt($GM / $a);

	$E_  = EccAnom($M,$e);   

	$cosE = cos($E_); 
	$sinE = sin($E_);

	$fac = sqrt ( (1.0-$e)*(1.0+$e) );  

	$rho = 1.0 - $e*$cosE;

	$r = Vec3D::getNewInstance1 ($a*($cosE-$e),  $a*$fac*$sinE,     0.0);
	$v = Vec3D::getNewInstance1 (-$k*$sinE/$rho, $k*$fac*$cosE/$rho, 0.0); 
}

//------------------------------------------------------------------------------
//
// EccAnom: computes the eccentric anomaly for elliptic orbits
//
// Input:
//
//   M        Mean anomaly in [rad]
//   e        Eccentricity of the orbit [0,1[
//
// <return>:  Eccentric anomaly in [rad]
//
//------------------------------------------------------------------------------
function EccAnom ($M, $e)
{
	//
	// Constants
	//
	$eps_mach = 2.2204460492503131e-016;
	$maxit = 15;
	$eps = 100.0 * $eps_mach;


	//
	// Variables
	//
	$i=0;
	$E_ = null;
	$f = null;


	// Starting value
	$M = Modulo($M, 2.0*pi);   
	if ($e<0.8) $E_ = $M; else $E_ = pi;


	// Iteration
	do {
		$f = $E_ - $e*sin($E_) - $M;
		$E_ = $E_ - $f / ( 1.0 - $e*cos($E_) );
		$i++;
		if ($i == $maxit) {
		  break;
		}
	}
	while (abs($f) > $eps);

	return $E_;
}

//------------------------------------------------------------------------------
//
// GaussVec: computes the transformation matrix from the orbital plane 
//           coordinate system to the ecliptic
//
// Input:
//
//   Omega    Longitude of the ascending node of the orbit in [rad]
//   i        Inclination of the orbit to the ecliptic in [rad]
//   omega    Argument of perihelion in [rad]
//
// <return>:  Transformation matrix containing the Gaussian vectors P, Q and R
//
//------------------------------------------------------------------------------
function GaussVec ($Omega_, $i, $omega)
{
	return Mat3D::MatrixProduct(Mat3D::R_z(-$Omega_), Mat3D::MatrixProduct(Mat3D::R_x(-$i), Mat3D::R_z(-$omega)));
}

?>
