<?php

include_once('Enum.php');

//
// Types
//

// identification for components of Vec3D objects
class Enum_index extends Enum { 
	const X = 0;
	const Y = 1;
	const Z = 2;
};

// declarations for polar angle representation of Vec3D objects
class Enum_pol_index extends Enum { 
	const PHI   = 0;  // azimuth of vector
	const THETA = 1;  // altitude of vector
	const R     = 2;  // norm of vector
};

//  Declaration of struct Polar
class Polar {  
	// Members
	public $phi;    // azimuth of vector
	public $theta;  // altitude of vector
	public $r;      // norm of vector

	// Constructors
	final public function __construct($Az = null, $Elev = null, $R = null){
		if($Az == null) $Az = 0;
		if($Elev == null) $Elev = 0;
		if($R == null) $R = 1;
		
		$this->phi = $Az;
		$this->theta = $Elev;
		$this->r = $R;
	}

};

class Vec3D {
	// Members
	private $m_Vec = array(0.0, 0.0, 0.0);        // components of vector
	private $m_phi = 0.0;           // polar angle (azimuth)
	private $m_theta = 0.0;         // polar angle (altitude)
	private $m_r = 0.0;             // norm of vector
	private $m_bPolarValid = false;   // status flag for validity of polar coordinates
	
	private static $friendClasses = array('Mat3D');  
	private static $friendFunctions = array();  
	
	public static function getNewInstance(){
		$vec = new Vec3D();
		return $vec;
	}
	
	public static function getNewInstance1($X, $Y, $Z){
		$vec = new Vec3D();
		$vec->m_Vec[0] = $X;
		$vec->m_Vec[1] = $Y;
		$vec->m_Vec[2] = $Z;
		return $vec;
	}
	
	public static function getNewInstance2(Polar $p){
		$vec = new Vec3D();
		$vec->m_bPolarValid = true;
		$vec->m_theta = $p->theta;
		$vec->m_phi = $p->phi;
		$vec->m_r = $p->r;
		
		$cosEl = cos($vec->m_theta);

		$vec->m_Vec[0] = $p->r * cos($vec->m_phi) * $cosEl;
		$vec->m_Vec[1] = $p->r * sin($vec->m_phi) * $cosEl;
		$vec->m_Vec[2] = $p->r * sin($vec->m_theta);
		
		return $vec;
	}
	
	//
	// Component access (index may be "x", "y", or "z")
	//
	function getByIndex (Enum_index $Index) {
		if($Index == new Enum_index('X')){
			return $this->m_Vec[0];
		} else if($Index == new Enum_index('Y')){
			return $this->m_Vec[1];
		} else if($Index == new Enum_index('Z')){
			return $this->m_Vec[2];
		}
	}
	
	//
	// Component access (index may be "x", "y", or "z")
	//
	function getByPolIndex (Enum_pol_index $PolIndex) {
		if ( !$this->m_bPolarValid ) {
			// Recalculate norm and polar angles of vector
			$this->CalcPolarAngles();
			$this->m_bPolarValid = true;
		}
		if($PolIndex == new Enum_pol_index('PHI')){
			return $this->m_phi;
		} else if($PolIndex == new Enum_pol_index('THETA')){
			return $this->m_theta;
		} else if($PolIndex == new Enum_pol_index('R')){
			return $this->m_r;
		}
	}
	
	// + pl
	// - mn
	// * ml
	// / dv
	// = eq
	
	function mn (){ //operator +=
		$this->m_Vec[0] += -$this->m_Vec[0];
		$this->m_Vec[1] += -$this->m_Vec[1];
		$this->m_Vec[2] += -$this->m_Vec[2];

		$this->m_bPolarValid = false;
	}
	
	//
	// In-place addition of vector
	//
	function pleq (Vec3D $Vec){ //operator +=
		$this->m_Vec[0] += $Vec->m_Vec[0];
		$this->m_Vec[1] += $Vec->m_Vec[1];
		$this->m_Vec[2] += $Vec->m_Vec[2];

		$this->m_bPolarValid = false;
	}
	
	//
	// In-place subtraction of vector
	//
	function mneq (Vec3D $Vec){ //operator -=
		$this->m_Vec[0] -= $Vec->m_Vec[0];
		$this->m_Vec[1] -= $Vec->m_Vec[1];
		$this->m_Vec[2] -= $Vec->m_Vec[2];

		$this->m_bPolarValid = false;
	}
	
	function mleq ($fScalar) //operator *=
	{
		$this->m_Vec[0] *= $fScalar;
		$this->m_Vec[1] *= $fScalar;
		$this->m_Vec[2] *= $fScalar;

		$this->m_bPolarValid = false;
	}
	
	function dveq ($fScalar) //operator *=
	{
		$this->m_Vec[0] /= $fScalar;
		$this->m_Vec[1] /= $fScalar;
		$this->m_Vec[2] /= $fScalar;

		$this->m_bPolarValid = false;
	}
	
	// On-demand calculation of polar components
	private function CalcPolarAngles (){
		// Length of projection in x-y-plane:
		$rhoSqr = $this->m_Vec[0] * $this->m_Vec[0] + $this->m_Vec[1] * $this->m_Vec[1]; 

		// Norm of vector
		$this->m_r = sqrt ( $rhoSqr + $this->m_Vec[2] * $this->m_Vec[2] );

		// Azimuth of vector
		if ( ($this->m_Vec[0] == 0.0) && ($this->m_Vec[1] == 0.0) ) {
			$this->m_phi = 0.0;
		} else {
			$this->m_phi = atan2 ($this->m_Vec[1], $this->m_Vec[0]);
		}
		if ( $this->m_phi < 0.0 ) {
			$this->m_phi += 2.0 * pi;
		}

		// Altitude of vector
		$rho = sqrt ( $rhoSqr );
		if ( ($this->m_Vec[2] == 0.0) && ($rho == 0.0) ) {
			$this->m_theta = 0.0;
		} else {
			$this->m_theta = atan2($this->m_Vec[2], $rho);
		}
	}
	
	//
	// Dot product
	//
	static public function Dot (Vec3D $left, Vec3D $right)
	{
		return $left->m_Vec[0] * $right->m_Vec[0] +
			   $left->m_Vec[1] * $right->m_Vec[1] +
			   $left->m_Vec[2] * $right->m_Vec[2];
	}
	
	//
	// Norm of vector
	//
	static public function Norm (Vec3D $Vec)
	{
		return sqrt(self::Dot($Vec, $Vec));
	}
	
	//
	// Scalar multiplication
	//
	public static function ScalarMultiplication (Vec3D $Vec, $fScalar) //operator *
	{
		return Vec3D::getNewInstance1 ( $Vec->m_Vec[0] * $fScalar, 
                                       $Vec->m_Vec[1] * $fScalar,
                                       $Vec->m_Vec[2] * $fScalar );
	}
	
	//
	// Scalar division
	//
	public static function ScalarDivision (Vec3D $Vec, $fScalar) //operator /
	{
		return Vec3D::getNewInstance1 ( $Vec->m_Vec[0] / $fScalar, 
                                       $Vec->m_Vec[1] / $fScalar,
                                       $Vec->m_Vec[2] / $fScalar );
	}
	
	//
	// Unary minus of vector
	//
	public static function UnaryMinusOfVector (Vec3D $Vec) //operator - 
	{
		return Vec3D::getNewInstance1( -$Vec->m_Vec[0], -$Vec->m_Vec[1], -$Vec->m_Vec[2] );
	}
	
	//
	// Addition of vectors
	//
	static public function AdditionOfVectors (Vec3D $left, Vec3D $right) //operator +
	{
		return Vec3D::getNewInstance1 ( $left->m_Vec[0] + $right->m_Vec[0], 
										$left->m_Vec[1] + $right->m_Vec[1], 
										$left->m_Vec[2] + $right->m_Vec[2]  );
	}
	
	//
	// Subtraction of vectors
	//
	static public function SubtractionOfVectors (Vec3D $left, Vec3D $right) //operator -  
	{
		return Vec3D::getNewInstance1 ( $left->m_Vec[0] - $right->m_Vec[0], 
										$left->m_Vec[1] - $right->m_Vec[1], 
										$left->m_Vec[2] - $right->m_Vec[2]  );
	}
	
	//
	// Vector product
	//
	static public function Cross(Vec3D $left, Vec3D $right)
	{
		return Vec3D::getNewInstance1 ( $left->m_Vec[1] * $right->m_Vec[2] - $left->m_Vec[2] * $right->m_Vec[1], 
										$left->m_Vec[2] * $right->m_Vec[0] - $left->m_Vec[0] * $right->m_Vec[2], 
										$left->m_Vec[0] * $right->m_Vec[1] - $left->m_Vec[1] * $right->m_Vec[0] );
	}
	
	//
	// Component access: retrieve given column of Matrix Mat
	//
	static public function Col(Mat3D $Mat, Enum_index $Index)
	{
		$Res = new Vec3D();

		for ($i = 0; $i < 3; $i++) {
			$Res->m_Vec[$i] = $Mat->m_Mat[$i][$Index->toInt()];
		}

		return $Res;

	}
	
	//
	// Component access: retrieve given row of Matrix Mat
	//
	static public function Row(Mat3D $Mat, Enum_index $Index)
	{
		$Res = new Vec3D();

		for ($j = 0; $j < 3; $j++) {
			$Res->m_Vec[$j] = $Mat->m_Mat[$Index->toInt()][$j];
		}

		return $Res;
	}
	
	//
	// Matrix-vector product
	//
	static public function MatrixVectorProduct (Mat3D $Mat, Vec3D $Vec)
	{
		$Result = new Vec3D();

		for ($i = 0; $i < 3; $i++) {
			$Scalp = 0.0;
			for ($j = 0; $j < 3; $j++){
				$Scalp += $Mat->m_Mat[$i][$j] * $Vec->m_Vec[$j];
			}
			$Result->m_Vec[$i] = $Scalp;
		}

		return $Result;
	}
	
	//
	// Vector-matrix product
	//
	static public function VectorMatrixProduct (Vec3D $Vec, Mat3D $Mat)
	{
		$Result = new Vec3D();

		for ($j = 0; $j < 3; $j++) {
			$Scalp = 0.0;
			for ($i = 0; $i < 3; $i++){
				$Scalp += $Vec->m_Vec[$i] * $Mat->m_Mat[$i][$j] ;
			}
			$Result->m_Vec[$j] = $Scalp;
		}

		return $Result;
	}
	
    public function __get($name) {
		$trace = debug_backtrace(DEBUG_BACKTRACE_IGNORE_ARGS);
        
		if ((isset($trace[1]['class']) && in_array($trace[1]['class'], static::$friendClasses)) ||
		    (isset($trace[1]['function']) && in_array($trace[1]['function'], static::$friendFunctions))) {  
            return $this->$name;  
        } else {  
            trigger_error('Member not available: ' . $name, E_USER_ERROR);  
        }  
    }  
}

//
// Mat3D: 3 dimensional transformation matrices
//
class Mat3D {
	private $m_Mat = array(array(0.0,0.0,0.0),array(0.0,0.0,0.0),array(0.0,0.0,0.0));  // matrix elements
	
	private static $friendClasses = array('Vec3D');  
	private static $friendFunctions = array('Col');  
	
	public static function getNewInstance(){
		$vec = new Mat3D();
		return $vec;
	}
	
	//
	// Constructor for matrix from column vectors
	//
	public static function getNewInstance1(Vec3D $e_1, Vec3D $e_2, Vec3D $e_3){
		$mat = new Mat3D();
		for ($i = 0; $i < 3; $i++) {
			$mat->m_Mat[$i][0] = $e_1->m_Vec[$i];
			$mat->m_Mat[$i][1] = $e_2->m_Vec[$i];
			$mat->m_Mat[$i][2] = $e_3->m_Vec[$i];
		}
		return $mat;
	}
	
	//
	// Create identity matrix
	//
	public static function Id3D()
	{
		$Id = new Mat3D();

		for ($i = 0; $i < 3; $i++){
			for ($j = 0; $j < 3; $j++){
				$Id->m_Mat[$i][$j] = ($i == $j) ? 1 : 0;
			}
		}
		return $Id;
	}
	
	//
	// Elementary rotation matrices
	//
	public static function R_x($RotAngle){
		$S = sin ($RotAngle);
		$C = cos ($RotAngle);

		$U = new Mat3D();

		$U->m_Mat[0][0] = 1.0;  $U->m_Mat[0][1] = 0.0;  $U->m_Mat[0][2] = 0.0;
		$U->m_Mat[1][0] = 0.0;  $U->m_Mat[1][1] = +$C;  $U->m_Mat[1][2] = +$S;
		$U->m_Mat[2][0] = 0.0;  $U->m_Mat[2][1] = -$S;  $U->m_Mat[2][2] = +$C;

		return $U;
	}
	
	public static function R_y($RotAngle){
		$S = sin ($RotAngle);
		$C = cos ($RotAngle);

		$U = new Mat3D();

		$U->m_Mat[0][0] = +$C;  $U->m_Mat[0][1] = 0.0;  $U->m_Mat[0][2] = -$S;
		$U->m_Mat[1][0] = 0.0;  $U->m_Mat[1][1] = 1.0;  $U->m_Mat[1][2] = 0.0;
		$U->m_Mat[2][0] = +$S;  $U->m_Mat[2][1] = 0.0;  $U->m_Mat[2][2] = +$C;

		return $U;
	}


	public static function R_z($RotAngle){
		$S = sin ($RotAngle);
		$C = cos ($RotAngle);

		$U = new Mat3D();

		$U->m_Mat[0][0] = +$C;  $U->m_Mat[0][1] = +$S;  $U->m_Mat[0][2] = 0.0;
		$U->m_Mat[1][0] = -$S;  $U->m_Mat[1][1] = +$C;  $U->m_Mat[1][2] = 0.0;
		$U->m_Mat[2][0] = 0.0;  $U->m_Mat[2][1] = 0.0;  $U->m_Mat[2][2] = 1.0;

		return $U;
	}
	
	//
	// Transpose of matrix
	//
	public static function Transp(Mat3D $Mat){
		$T = new Mat3D();

		for ( $i = 0; $i < 3; $i++ ){
			for ( $j = 0; $j < 3; $j++ ){
				$T->m_Mat[$i][$j] = $Mat->m_Mat[$j][$i];
			}
		}
		
		return $T;
	}
	
	//
	// Scalar multiplication
	//
	public static function ScalarMultiplication (Mat3D $Mat, $fScalar){
		$Result = new Mat3D();

		for ($i = 0; $i < 3; $i++){
			for ($j = 0; $j < 3; $j++){
				$Result->m_Mat[$i][$j] = $Mat->m_Mat[$i][$j] * $fScalar;
			}
		}

		return $Result;
	}
	
	//
	// Scalar division
	//
	public static function ScalarDivision (Mat3D $Mat, $fScalar){
		$Result = new Mat3D();

		for ($i = 0; $i < 3; $i++){
			for ($j = 0; $j < 3; $j++){
				$Result->m_Mat[$i][$j] = $Mat->m_Mat[$i][$j] / $fScalar;
			}
		}

		return $Result;
	}
	
	//
	// Unary minus of matrix
	//
	public static function UnaryMinusOfMatrix (Mat3D $Mat){
		$Result = new Mat3D();

		for ($i = 0; $i < 3; $i++){
			for ($j = 0; $j < 3; $j++){
				$Result->m_Mat[$i][$j] = -$Mat->m_Mat[$i][$j];
			}
		}
			
		return $Result;
	}
	
	//
	// Addition of matrices
	//
	public static function AdditionOfMatrices (Mat3D $left, Mat3D $right){ // operator +
		$Result = new Mat3D();

		for ($i = 0; $i < 3; $i++) {
			for ($j = 0; $j < 3; $j++) {
				$Result->m_Mat[$i][$j] = $left->m_Mat[$i][$j] + $right->m_Mat[$i][$j];
			}
		}

		return $Result;
	}
	
	//
	// Subtraction of matrices
	//
	public static function SubtractionOfMatrices (Mat3D $left, Mat3D $right){ // operator -
		$Result = new Mat3D();

		for ($i = 0; $i < 3; $i++) {
			for ($j = 0; $j < 3; $j++) {
				$Result->m_Mat[$i][$j] = $left->m_Mat[$i][$j] - $right->m_Mat[$i][$j];
			}
		}

		return $Result;
	}
	
	//
	// Matrix product
	//
	public static function MatrixProduct (Mat3D $left, Mat3D $right){ // operator *
		$Result = new Mat3D();

		for ($i = 0; $i < 3; $i++) {
			for ($j = 0; $j < 3; $j++) {
				$Scalp = 0.0;
				for ($k = 0; $k < 3; $k++)
				$Scalp += $left->m_Mat[$i][$k] * $right->m_Mat[$k][$j];
				$Result->m_Mat[$i][$j] = $Scalp;
			}
		}

		return $Result;
	}

	public function __get($name) {
		$trace = debug_backtrace(DEBUG_BACKTRACE_IGNORE_ARGS);
        
		if ((isset($trace[1]['class']) && in_array($trace[1]['class'], static::$friendClasses)) ||
		    (isset($trace[1]['function']) && in_array($trace[1]['function'], static::$friendFunctions))) {  
            return $this->$name;  
        } else {  
            trigger_error('Member not available: ' . $name, E_USER_ERROR);  
        }  
    }  
}

?>
