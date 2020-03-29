<?php

include_once('APC_VecMat3d.php');

class Cheb3D {
    // Members
    
    private $m_f = null;             // Function
    private $m_n = null;             // Degree 
    private $m_Valid = null;         // Status flag for validity of Chebyshev coefficients
    private $m_dt = null;            // Interval size
    private $m_ta = null;       // Interval
	private $m_tb = null;
    private $Cx = array();     // Chebyshev coefficients
	private $Cy = array();
	private $Cz = array();
	
	function __construct($f, $n, $dt) {
		$this->m_f = $f;
		$this->m_n = $n;
		$this->m_dt = $dt;
		$this->m_Valid = false;
	}
	
	//
	// Fit: approximates function m_f over the interval [ta, tb]
	//
	public function Fit ($ta, $tb) {
		//
		// Variables
		//
		$j = null; 
		$k = null;
		$tau = null;
		$t = null;
		$fac;
		$T = array();
		$f = new Vec3d();
		$m_f = $this->m_f;


		// Clear all coefficients
		for ($j = 0; $j <= $this->m_n; $j++) {
			$this->Cx[$j] = 0;
			$this->Cy[$j] = 0;
			$this->Cz[$j] = 0; 
		}

		// Loop over roots of T^(n+1)
		for ($k = 0; $k <= $this->m_n; $k++) {

			$tau = cos((2*$k+1)*pi/(2*$this->m_n+2));         // Grid point tau_k
			$t = (($tb-$ta)/2.0) * $tau + (($tb+$ta)/2.0); // Time t
			$f = $m_f($t);                              // Function value at time t

			// Calculate coefficients C_j using recurrence relation
			for ($j = 0; $j <= $this->m_n; $j++) {
				switch ($j) {
				case  0: $T[$j]=1.0; break;
				case  1: $T[$j]=$tau; break;
				default: $T[$j]=2.0*$tau*$T[$j-1]-$T[$j-2];
				};
				// Increment coefficient C_j by f(t)*T_j(tau)
				$this->Cx[$j]+=$f->getByIndex(new Enum_index("X"))*$T[$j]; 
				$this->Cy[$j]+=$f->getByIndex(new Enum_index("Y"))*$T[$j];
				$this->Cz[$j]+=$f->getByIndex(new Enum_index("Z"))*$T[$j];  
			}; 
		};

		// Scaling
		$fac = 2.0/($this->m_n+1);
		for ($j = 0; $j <= $this->m_n; $j++) { 
			$this->Cx[$j]*=$fac;
			$this->Cy[$j]*=$fac;
			$this->Cz[$j]*=$fac; 
		}

		// Update some members
		$this->m_ta = $ta;
		$this->m_tb = $tb;

		$this->m_Valid = true;
	}
	
	//
	// Value: evaluation of approximation at argument t
	//
	public function Value ($t) {
		//
		// Constants
		//
		$eps = 0.01; // Fractional overlap

		//
		// Variables
		//
		$f1 = new Vec3D(); 
		$f2 = new Vec3D(); 
		$old_f1 = new Vec3D();
		$tau = null;
		$k = null;


		// Generate new coefficients as required
		if ( !$this->m_Valid || ($t < $this->m_ta) || ($this->m_tb < $t) ) {
			$k = floor($t / $this->m_dt); 
			$this->Fit ( ($k - $eps) * $this->m_dt, ($k + 1 + $eps) * $this->m_dt );
		}

		// Evaluate approximation
		$tau = (2.0 * $t - $this->m_ta - $this->m_tb) / ($this->m_tb - $this->m_ta);  

		for ($i = $this->m_n; $i >= 1; $i--) {
			$old_f1 = $f1; 
			$f1 = Vec3D::AdditionOfVectors(Vec3D::SubtractionOfVectors(Vec3D::ScalarMultiplication($f1, 2.0 * $tau), $f2), Vec3D::getNewInstance1($this->Cx[$i], $this->Cy[$i], $this->Cz[$i]));
			$f2 = $old_f1;
		};
  
		return Vec3D::AdditionOfVectors(Vec3D::SubtractionOfVectors(Vec3D::ScalarMultiplication($f1, $tau), $f2), Vec3D::ScalarMultiplication(Vec3D::getNewInstance1($this->Cx[0],$this->Cy[0],$this->Cz[0]),0.5));
	}
}

?>
