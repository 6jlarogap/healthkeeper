<?php

include_once('APC_Const.php');

//------------------------------------------------------------------------------
//
// Ddd: Conversion of angular degrees, minutes and seconds of arc to decimal
//   representation of an angle 
//
// Input:
//
//   D        Angular degrees
//   M        Minutes of arc
//   S        Seconds of arc
//
// <return>:  Angle in decimal representation
//
//------------------------------------------------------------------------------
function Ddd($D, $M, $S)
{
	//
	// Variables
	//
	$sign = null;


	if ( ($D < 0) || ($M < 0) || ($S < 0) ) {
		$sign = -1.0;
	} else { 
		$sign = 1.0;
	}

	return  $sign * ( abs($D) + abs($M) / 60.0 + abs($S) / 3600.0 );
}

//------------------------------------------------------------------------------
//
// Frac: Gives the fractional part of a number
//
//------------------------------------------------------------------------------

function Frac ($x)
{
   return $x - floor($x);
}

//------------------------------------------------------------------------------
//
// Quad: Quadratic interpolation
//
//       Performs root finding and search for extreme values based on three 
//       equidistant values of a function.
//
// Input:
//
//   y_minus   Value of function at x = -1
//   y_0       Value of function at x =  0
//   y_plus    Value of function at x =  1
//
// Output:
//
//   xe        Abscissa of extremum (may be outside [-1, 1])
//   ye        Value of function at xe
//   root1     First root found
//   root2     Second root found
//   n_root    Number of roots found in [-1, 1]
//
// Notes:
//   
//   Roots and location of an extremum are calculated from a parabola through
//   the given functional values.
//   To investigate functional values for other abscissas than -1, 0 and 1,
//   a linear transformation of given values can be applied, if the given 
//   abscissae are spaced equally.
//
//------------------------------------------------------------------------------
function Quad ( $y_minus, $y_0, $y_plus, &$xe, &$ye, &$root1, &$root2, &$n_root )
{
	//
	// Variables
	//
	$a = null;
	$b = null;
	$c = null;
	$dis = null;
	$dx = null;


	$n_root = 0;

	// Coefficients of interpolating parabola y=a*x^2+b*x+c
	$a  = 0.5 * ($y_plus + $y_minus) - $y_0;
	$b  = 0.5 * ($y_plus - $y_minus);
	$c  = $y_0;

	// Find extreme value
	$xe = -$b / (2.0 * $a); 
	$ye = ($a * $xe + $b) * $xe + $c;

	$dis = $b * $b - 4.0 * $a * $c; // Discriminant of y=a*x^2+b*x+c

	if ($dis >= 0) // Parabola has roots 
	{
		$dx = 0.5 * sqrt ($dis) / abs ($a);

		$root1 = $xe - $dx; 
		$root2 = $xe + $dx;

		if (abs($root1) <= 1.0) ++$n_root;  
		if (abs($root2) <= 1.0) ++$n_root;
		if ($root1       < -1.0) $root1 = $root2;
	}
}

//------------------------------------------------------------------------------
//
// AddThe: Calculates cos(alpha+beta) and sin(alpha+beta) using addition 
//         theorems
//
// Input:
//
//   c1        cos(alpha)
//   s1        sin(alpha)
//   c2        cos(beta)
//   s2        sin(beta)
//
// Output:
//
//   c         cos(alpha+beta)
//   s         sin(alpha+beta)
//
//------------------------------------------------------------------------------
function AddThe ($c1, $s1, $c2, $s2, &$c, &$s )
{
  $c = $c1 * $c2 - $s1 * $s2; 
  $s = $s1 * $c2 + $c1 * $s2;
}

//------------------------------------------------------------------------------
//
// Pegasus: Root finder using the Pegasus method
//
// Input:
//
//   PegasusFunct  Pointer to the function to be examined
//
//   LowerBound    Lower bound of search interval
//   UpperBound    Upper bound of search interval
//   Accuracy      Desired accuracy for the root
//
// Output:
//
//   Root          Root found (valid only if Success is true)
//   Success       Flag indicating success of the routine
//
// References:                                                               
//
//   Dowell M., Jarratt P., 'A modified Regula Falsi Method for Computing    
//     the root of an equation', BIT 11, p.168-174 (1971).                   
//   Dowell M., Jarratt P., 'The "PEGASUS Method for Computing the root      
//     of an equation', BIT 12, p.503-508 (1972).                            
//   G.Engeln-Muellges, F.Reutter, 'Formelsammlung zur Numerischen           
//     Mathematik mit FORTRAN77-Programmen', Bibliogr. Institut,             
//     Zuerich (1986).                                                       
//
// Notes:
//
//   Pegasus assumes that the root to be found is bracketed in the interval
//   [LowerBound, UpperBound]. Ordinates for these abscissae must therefore
//   have different signs.
//
//------------------------------------------------------------------------------
function Pegasus ( $f, $LowerBound, $UpperBound, $Accuracy, &$Root, &$Success ){
	//
	// Constants
	//
	$MaxIterat = 30; 

	//
	// Variables
	//
	$x1 = $LowerBound; 
	$f1 = $f($x1);
	$x2 = $UpperBound;
	$f2 = $f($x2);
	$x3 = 0.0;
	$f3 = 0.0;

	$Iterat = 0; 


	// Initialization
	$Success = false;
	$Root    = $x1;


	// Iteration
	if ( $f1 * $f2 < 0.0 )
	do 
	{
		// Approximation of the root by interpolation
		$x3 = $x2 - $f2/( ($f2-$f1)/($x2-$x1) ); $f3 = $f($x3);

		// Replace (x1,f2) and (x2,f2) by new values, such that
		// the root is again within the interval [x1,x2]
		if ( $f3 * $f2 <= 0.0 ) {
			// Root in [x2,x3]
			$x1 = $x2; $f1 = $f2; // Replace (x1,f1) by (x2,f2)
			$x2 = $x3; $f2 = $f3; // Replace (x2,f2) by (x3,f3)
		}
		else {
			// Root in [x1,x3]
			$f1 = $f1 * $f2/($f2+$f3); // Replace (x1,f1) by (x1,f1')
			$x2 = $x3; $f2 = $f3;     // Replace (x2,f2) by (x3,f3)
		}

		if (abs($f1) < abs($f2))
			$Root = $x1;
		else
			$Root = $x2;

		if(abs($x2-$x1) <= $Accuracy)
			$Success = true;
		else
			$Success = false;
		$Iterat++;
	}
	while ( !$Success && ($Iterat < $MaxIterat) );
}

?>
