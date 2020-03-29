<?php

include_once('APC_Const.php');
include_once('APC_VecMat3D.php');
	
//------------------------------------------------------------------------------
//
// Equ2EclMatrix: Transformation of equatorial to ecliptical coordinates
//
// Input:
//
//   T         Time in Julian centuries since J2000
//
// <return>:   Transformation matrix
//
//------------------------------------------------------------------------------
function Equ2EclMatrix ($T)
{
  //
  // Constants
  //
  $eps = ( 23.43929111 - (46.8150 + (0.00059 - 0.001813 * $T) * $T) * $T / 3600.0 ) * Rad;
   
  return Mat3D::R_x($eps);
}

//------------------------------------------------------------------------------
//
// Ecl2EquMatrix: Transformation of ecliptical to equatorial coordinates
//
// Input:
//
//   T         Time in Julian centuries since J2000
//
// <return>:   Transformation matrix
//
//------------------------------------------------------------------------------
function Ecl2EquMatrix ($T)
{
  return Mat3D::Transp(Equ2EclMatrix($T));
}

//------------------------------------------------------------------------------
//
// Site: Calculates the geocentric position of a site on the Earth's surface
//
// Input:
//
//   lambda    Geographical longitude (east positive) in [rad]
//   phi       Geographical latitude  in [rad]
//
// <return>:   Geocentric position in [km]
//
//------------------------------------------------------------------------------
function Site ($lambda, $phi) {
	//
	// Constants
	//
	$f     = 1.0/298.257;   // Flattening 
	$e_sqr = $f*(2.0-$f);     // Square of eccentricity
	$cos_phi = cos($phi);    // (Co)sine of geographical latitude
	$sin_phi = sin($phi);


	//
	// Variables
	//
	$N = R_Earth / sqrt (1.0-$e_sqr*($sin_phi*$sin_phi));
	  

	// Cartesian position vector [km]
	return Vec3D::getNewInstance1 ( $N*$cos_phi*cos($lambda),
				                    $N*$cos_phi*sin($lambda),
				                    (1.0-$e_sqr)*$N*$sin_phi  );
}

?>
