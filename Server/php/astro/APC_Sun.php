<?php

include_once('APC_Const.php');
include_once('APC_Math.php');
include_once('APC_VecMat3D.php');

//
// Constants
//
const sun_o   = 10;         // Index offset
const sun_dim = 21;         // Work array dimension

//
// Definition of Pert class for summing up trigonometric perturbation 
// series
//
class SunPert {
	private $m_T;
	private $m_C_ = array();
	private $m_S_ = array();
	private $m_c = array();
	private $m_s = array();
	private $m_dl;
	private $m_db;
	private $m_dr;
	private $m_u;
	private $m_v;
	
	// Set time, mean anomalies and index range
	public function Init ( $T, $M_, $I_min_, $I_max_, $m, $i_min, $i_max ){
		//
		// Variables
		//
		$i = null;


		$this->m_dl = 0.0; $this->m_dr = 0.0; $this->m_db = 0.0; // reset perturbations

		$this->m_T = $T;  // set time


		// cosine and sine of multiples of M
		$this->m_C_[sun_o]=1.0; $this->m_C_[sun_o + 1]=cos($M_); $this->m_C_[sun_o - 1] = +$this->m_C_[sun_o + 1];
		$this->m_S_[sun_o]=0.0; $this->m_S_[sun_o + 1]=sin($M_); $this->m_S_[sun_o - 1] = -$this->m_S_[sun_o + 1];

		for ($i = 1; $i < $I_max_; $i++) {
			AddThe ( $this->m_C_[sun_o + $i],$this->m_S_[sun_o + $i], $this->m_C_[sun_o + 1],$this->m_S_[sun_o + 1], $this->m_C_[sun_o + $i + 1],$this->m_S_[sun_o + $i + 1] ); 
		}
		for ($i = -1; $i > $I_min_; $i--) { 
			AddThe ( $this->m_C_[sun_o + $i],$this->m_S_[sun_o + $i], $this->m_C_[sun_o - 1],$this->m_S_[sun_o - 1], $this->m_C_[sun_o + $i - 1],$this->m_S[sun_o + $i - 1] ); 
		}


		// cosine and sine of multiples of m
		$this->m_c[sun_o]=1.0; $this->m_c[sun_o + 1]=cos($m); $this->m_c[sun_o - 1] = +$this->m_c[sun_o + 1];
		$this->m_s[sun_o]=0.0; $this->m_s[sun_o + 1]=sin($m); $this->m_s[sun_o - 1] = -$this->m_s[sun_o + 1];

		for ($i = 1; $i < $i_max; $i++) { 
			AddThe ( $this->m_c[sun_o + $i],$this->m_s[sun_o + $i], $this->m_c[sun_o + 1],$this->m_s[sun_o + 1], $this->m_c[sun_o + $i + 1],$this->m_s[sun_o + $i + 1] );
		}
		for ($i = -1; $i > $i_min; $i--) { 
			AddThe ( $this->m_c[sun_o + $i],$this->m_s[sun_o + $i], $this->m_c[sun_o - 1],$this->m_s[sun_o - 1], $this->m_c[sun_o + $i - 1],$this->m_s[sun_o + $i - 1] );
		}
	}
	
	// Sum-up perturbations in longitude, radius and latitude
	public function Term ($I_, $i, $iT, $dlc, $dls, $drc, $drs, $dbc, $dbs ) {
		if ( $iT == 0 ) { 
			AddThe ( $this->m_C_[sun_o + $I_],$this->m_S_[sun_o + $I_], $this->m_c[sun_o + $i],$this->m_s[sun_o + $i], $this->m_u,$this->m_v );
		} else { 
			$this->m_u *= $this->m_T;
			$this->m_v *= $this->m_T; 
		}

		$this->m_dl += ( $dlc * $this->m_u + $dls * $this->m_v );
		$this->m_dr += ( $drc * $this->m_u + $drs * $this->m_v );
		$this->m_db += ( $dbc * $this->m_u + $dbs * $this->m_v );
	}
	
	// Retrieve perturbations in longitude, radius and latitude
	public function dl(){ 
		return $this->m_dl;
	}

	public function dr(){
		return $this->m_dr;
	}

	public function db(){
		return $this->m_db;
	}
}

//------------------------------------------------------------------------------
//
// MiniSun: Computes the Sun's RA and declination using a low precision 
//          analytical series
//
// Input:
//
//   T         Time in Julian centuries since J2000
//
// Output:
//
//   RA        Right Ascension of the Sun in [rad]
//   Dec       Declination of the Sun in [rad]
//
//------------------------------------------------------------------------------
function MiniSun ($T, &$RA, &$Dec)
{
	//
	// Constants
	//
	$eps = 23.43929111 * Rad; 


	//
	// Variables
	//
	$L = null;
	$M = null;
	$e_Sun = Vec3D::getNewInstance();


	// Mean anomaly and ecliptic longitude  
	$M  = pi2 * Frac ( 0.993133 + 99.997361*$T); 
	$L  = pi2 * Frac ( 0.7859453 + $M/pi2 + 
					(6893.0*sin($M)+72.0*sin(2.0*$M)+6191.2*$T) / 1296.0e3);

	// Equatorial coordinates
	$e_Sun = Vec3D::MatrixVectorProduct(Mat3D::R_x(-$eps), Vec3D::getNewInstance2(new Polar($L,0.0)));

	$RA  = $e_Sun->getByPolIndex(new Enum_pol_index('PHI'));
	$Dec = $e_Sun->getByPolIndex(new Enum_pol_index('THETA'));
}

//------------------------------------------------------------------------------
//
// SunPos: Computes the Sun's ecliptical position using analytical series
//
// Input:
//
//   T         Time in Julian centuries since J2000
//
// <return>:   Geocentric position of the Sun (in [AU]), referred to the
//             ecliptic and equinox of date
//
//------------------------------------------------------------------------------
function SunPos ($T)
{
	//
	// Variables
	//
	$M2 = null;          // Mean anomalies
	$M3 = null;
	$M4 = null;
	$M5 = null;
	$M6 = null;
	$D = null;                 // Mean arguments of lunar orbit
	$A = null;
	$U = null;
	$Ven = new SunPert();        // Perturbations
	$Mar = new SunPert();  
	$Jup = new SunPert();  
	$Sat = new SunPert();
	$dl = null;              // Corrections in longitude ["],
	$dr = null;
	$db = null;
								   
	$l = null;        // radius [AU] and latitude ["]
	$b = null;        // Ecliptic coordinates
	$r = null;


	// Mean anomalies of planets and mean arguments of lunar orbit [rad]
	$M2 = pi2 * Frac ( 0.1387306 + 162.5485917 * $T );
	$M3 = pi2 * Frac ( 0.9931266 +  99.9973604 * $T );
	$M4 = pi2 * Frac ( 0.0543250 +  53.1666028 * $T ); 
	$M5 = pi2 * Frac ( 0.0551750 +   8.4293972 * $T );
	$M6 = pi2 * Frac ( 0.8816500 +   3.3938722 * $T ); 

	$D  = pi2 * Frac ( 0.8274 + 1236.8531 * $T );
	$A  = pi2 * Frac ( 0.3749 + 1325.5524 * $T );      
	$U  = pi2 * Frac ( 0.2591 + 1342.2278 * $T );


	// Keplerian terms and perturbations by Venus
	$Ven->Init ( $T, $M3,0,7, $M2,-6,0 );

	$Ven->Term ( 1, 0,0,-0.22,6892.76,-16707.37, -0.54, 0.00, 0.00);
	$Ven->Term ( 1, 0,1,-0.06, -17.35,    42.04, -0.15, 0.00, 0.00);
	$Ven->Term ( 1, 0,2,-0.01,  -0.05,     0.13, -0.02, 0.00, 0.00);
	$Ven->Term ( 2, 0,0, 0.00,  71.98,  -139.57,  0.00, 0.00, 0.00);
	$Ven->Term ( 2, 0,1, 0.00,  -0.36,     0.70,  0.00, 0.00, 0.00);
	$Ven->Term ( 3, 0,0, 0.00,   1.04,    -1.75,  0.00, 0.00, 0.00);
	$Ven->Term ( 0,-1,0, 0.03,  -0.07,    -0.16, -0.07, 0.02,-0.02);
	$Ven->Term ( 1,-1,0, 2.35,  -4.23,    -4.75, -2.64, 0.00, 0.00);
	$Ven->Term ( 1,-2,0,-0.10,   0.06,     0.12,  0.20, 0.02, 0.00);
	$Ven->Term ( 2,-1,0,-0.06,  -0.03,     0.20, -0.01, 0.01,-0.09);
	$Ven->Term ( 2,-2,0,-4.70,   2.90,     8.28, 13.42, 0.01,-0.01);
	$Ven->Term ( 3,-2,0, 1.80,  -1.74,    -1.44, -1.57, 0.04,-0.06);
	$Ven->Term ( 3,-3,0,-0.67,   0.03,     0.11,  2.43, 0.01, 0.00);
	$Ven->Term ( 4,-2,0, 0.03,  -0.03,     0.10,  0.09, 0.01,-0.01);
	$Ven->Term ( 4,-3,0, 1.51,  -0.40,    -0.88, -3.36, 0.18,-0.10);
	$Ven->Term ( 4,-4,0,-0.19,  -0.09,    -0.38,  0.77, 0.00, 0.00);
	$Ven->Term ( 5,-3,0, 0.76,  -0.68,     0.30,  0.37, 0.01, 0.00);
	$Ven->Term ( 5,-4,0,-0.14,  -0.04,    -0.11,  0.43,-0.03, 0.00);
	$Ven->Term ( 5,-5,0,-0.05,  -0.07,    -0.31,  0.21, 0.00, 0.00);
	$Ven->Term ( 6,-4,0, 0.15,  -0.04,    -0.06, -0.21, 0.01, 0.00);
	$Ven->Term ( 6,-5,0,-0.03,  -0.03,    -0.09,  0.09,-0.01, 0.00);
	$Ven->Term ( 6,-6,0, 0.00,  -0.04,    -0.18,  0.02, 0.00, 0.00);
	$Ven->Term ( 7,-5,0,-0.12,  -0.03,    -0.08,  0.31,-0.02,-0.01);

	$dl = $Ven->dl(); $dr = $Ven->dr();  $db = $Ven->db();


	// Perturbations by Mars 
	$Mar->Init ( $T, $M3,1,5, $M4,-8,-1 );

	$Mar->Term ( 1,-1,0,-0.22,   0.17,    -0.21, -0.27, 0.00, 0.00);
	$Mar->Term ( 1,-2,0,-1.66,   0.62,     0.16,  0.28, 0.00, 0.00);
	$Mar->Term ( 2,-2,0, 1.96,   0.57,    -1.32,  4.55, 0.00, 0.01);
	$Mar->Term ( 2,-3,0, 0.40,   0.15,    -0.17,  0.46, 0.00, 0.00);
	$Mar->Term ( 2,-4,0, 0.53,   0.26,     0.09, -0.22, 0.00, 0.00);
	$Mar->Term ( 3,-3,0, 0.05,   0.12,    -0.35,  0.15, 0.00, 0.00);
	$Mar->Term ( 3,-4,0,-0.13,  -0.48,     1.06, -0.29, 0.01, 0.00);
	$Mar->Term ( 3,-5,0,-0.04,  -0.20,     0.20, -0.04, 0.00, 0.00);
	$Mar->Term ( 4,-4,0, 0.00,  -0.03,     0.10,  0.04, 0.00, 0.00);
	$Mar->Term ( 4,-5,0, 0.05,  -0.07,     0.20,  0.14, 0.00, 0.00);
	$Mar->Term ( 4,-6,0,-0.10,   0.11,    -0.23, -0.22, 0.00, 0.00);
	$Mar->Term ( 5,-7,0,-0.05,   0.00,     0.01, -0.14, 0.00, 0.00);
	$Mar->Term ( 5,-8,0, 0.05,   0.01,    -0.02,  0.10, 0.00, 0.00);

	$dl += $Mar->dl(); $dr += $Mar->dr();  $db += $Mar->db();


	// Perturbations by Jupiter 
	$Jup->Init ( $T, $M3,-1,3, $M5,-4,-1 );

	$Jup->Term (-1,-1,0, 0.01,   0.07,     0.18, -0.02, 0.00,-0.02);
	$Jup->Term ( 0,-1,0,-0.31,   2.58,     0.52,  0.34, 0.02, 0.00);
	$Jup->Term ( 1,-1,0,-7.21,  -0.06,     0.13,-16.27, 0.00,-0.02);
	$Jup->Term ( 1,-2,0,-0.54,  -1.52,     3.09, -1.12, 0.01,-0.17);
	$Jup->Term ( 1,-3,0,-0.03,  -0.21,     0.38, -0.06, 0.00,-0.02);
	$Jup->Term ( 2,-1,0,-0.16,   0.05,    -0.18, -0.31, 0.01, 0.00);
	$Jup->Term ( 2,-2,0, 0.14,  -2.73,     9.23,  0.48, 0.00, 0.00);
	$Jup->Term ( 2,-3,0, 0.07,  -0.55,     1.83,  0.25, 0.01, 0.00);
	$Jup->Term ( 2,-4,0, 0.02,  -0.08,     0.25,  0.06, 0.00, 0.00);
	$Jup->Term ( 3,-2,0, 0.01,  -0.07,     0.16,  0.04, 0.00, 0.00);
	$Jup->Term ( 3,-3,0,-0.16,  -0.03,     0.08, -0.64, 0.00, 0.00);
	$Jup->Term ( 3,-4,0,-0.04,  -0.01,     0.03, -0.17, 0.00, 0.00);

	$dl += $Jup->dl(); $dr += $Jup->dr();  $db += $Jup->db();


	// Perturbations by Saturn 
	$Sat->Init ( $T, $M3,0,2, $M6,-2,-1 );

	$Sat->Term ( 0,-1,0, 0.00,   0.32,     0.01,  0.00, 0.00, 0.00);
	$Sat->Term ( 1,-1,0,-0.08,  -0.41,     0.97, -0.18, 0.00,-0.01);
	$Sat->Term ( 1,-2,0, 0.04,   0.10,    -0.23,  0.10, 0.00, 0.00);
	$Sat->Term ( 2,-2,0, 0.04,   0.10,    -0.35,  0.13, 0.00, 0.00);

	$dl += $Sat->dl(); $dr += $Sat->dr();  $db += $Sat->db();


	// Difference of Earth-Moon-barycentre and centre of the Earth
	$dl += +  6.45 * sin($D) - 0.42 * sin($D - $A) + 0.18 * sin($D + $A)
		+  0.17 * sin($D - $M3) - 0.06 * sin($D + $M3);

	$dr += + 30.76 * cos($D) - 3.06 * cos($D - $A) + 0.85 * cos($D + $A)
		-  0.58 * cos($D + $M3) + 0.57 * cos($D - $M3);

	$db += + 0.576 * sin($U);


	// Long-periodic perturbations
	$dl += + 6.40 * sin ( pi2*(0.6983 + 0.0561 * $T) ) 
		+ 1.87 * sin ( pi2*(0.5764 + 0.4174 * $T) )
		+ 0.27 * sin ( pi2*(0.4189 + 0.3306 * $T) ) 
		+ 0.20 * sin ( pi2*(0.3581 + 2.4814 * $T) );


	// Ecliptic coordinates ([rad],[AU])
	$l = pi2 * Frac ( 0.7859453 + $M3/pi2 +
				 ( (6191.2+1.1*$T)*$T + $dl ) / 1296.0e3 );
	$r = 1.0001398 - 0.0000007 * $T + $dr * 1.0e-6;
	$b = $db / Arcs;

	return Vec3D::getNewInstance2 (new Polar($l,$b,$r) );  // Position vector
}

//------------------------------------------------------------------------------
//
// SunEqu: Computes the Sun's equatorial position using analytical series
//
// Input:
//
//   T         Time in Julian centuries since J2000
//
// <return>:   Geocentric position of the Sun (in [AU]), referred to the
//             equator and equinox of date
//
//------------------------------------------------------------------------------
function SunEqu ( $T )
{
	return Vec3D::MatrixVectorProduct(Mat3D::MatrixProduct(NutMatrix($T), Ecl2EquMatrix($T)), SunPos($T));
}

?>
