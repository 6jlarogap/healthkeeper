<?php

include_once('APC_Const.php');
include_once('APC_VecMat3D.php');

//------------------------------------------------------------------------------
//
// PrecMatrix_Ecl: Precession of ecliptic coordinates
//
// Input:
//
//   T1        Epoch given
//   T2        Epoch to precess to
//
// <return>:   Precession transformation matrix
//
// Note: T1 and T2 in Julian centuries since J2000
//
//------------------------------------------------------------------------------
function PrecMatrix_Ecl ($T1, $T2)
{
  //
  // Constants
  //
  $dT = $T2 - $T1;
  

  //
  // Variables
  //
  $Pi_ = null;
  $pi = null;
  $p_a = null;

  
  $Pi_  = 174.876383889*Rad + 
        ( ((3289.4789+0.60622*$T1)*$T1) +
              ((-869.8089-0.50491*$T1) + 0.03536*$dT)*$dT )/Arcs;
  $pi  = ( (47.0029-(0.06603-0.000598*$T1)*$T1)+
              ((-0.03302+0.000598*$T1)+0.000060*$dT)*$dT )*$dT/Arcs;
  $p_a = ( (5029.0966+(2.22226-0.000042*$T1)*$T1)+
              ((1.11113-0.000042*$T1)-0.000006*$dT)*$dT )*$dT/Arcs;
    
  return Mat3D::MatrixProduct(Mat3D::R_z(-($Pi_+$p_a)), Mat3D::MatrixProduct(Mat3D::R_x($pi), Mat3D::R_z($Pi_)));
}  

//------------------------------------------------------------------------------
//
// NutMatrix: Transformation from mean to true equator and equinox
//
// Input:
//
//   T         Time in Julian centuries since epoch J2000
//
// <return>:   Nutation matrix
//
//------------------------------------------------------------------------------
function NutMatrix ($T)
{
  //
  // Variables
  //
  $ls = null;
  $D = null;
  $F = null;
  $N = null;
  $eps = null;
  $dpsi = null;
  $deps = null;

  // Mean arguments of lunar and solar motion
  $ls = pi2*Frac(0.993133+  99.997306*$T);   // mean anomaly Sun          
  $D  = pi2*Frac(0.827362+1236.853087*$T);   // diff. longitude Moon-Sun  
  $F  = pi2*Frac(0.259089+1342.227826*$T);   // mean argument of latitude 
  $N  = pi2*Frac(0.347346-   5.372447*$T);   // longit. ascending node    

  // Nutation angles
  $dpsi = ( -17.200*sin($N)   - 1.319*sin(2*($F-$D+$N)) - 0.227*sin(2*($F+$N))
            + 0.206*sin(2*$N) + 0.143*sin($ls) ) / Arcs;
  $deps = ( + 9.203*cos($N)   + 0.574*cos(2*($F-$D+$N)) + 0.098*cos(2*($F+$N))
            - 0.090*cos(2*$N)                 ) / Arcs;

  // Mean obliquity of the ecliptic 
  $eps  = 0.4090928-2.2696E-4*$T;

  return Mat3D::MatrixProduct(Mat3D::R_x(-$eps-$deps), Mat3D::MatrixProduct(Mat3D::R_z(-$dpsi), Mat3D::R_x(+$eps)));
}

?>
