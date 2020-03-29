<?php
include_once('Sunset.php');
include_once('Planrise.php');
include_once('Planpos.php');
include_once('Phases.php');
include_once('EclTimer.php');


echo "<br><br>Sunset";

Sunset\Sunset(2014,2,16,26,52,3,'MOON',$r,$s);
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>set=" . date("Y-m-d H:i:s",$s);


echo "<br><br>Planrise";

Planrise\Planrise(2014,2,16,26,52,3,'MERCURY',$r,$s,$t);
echo "<br>--MERCURY--";
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>trans=" . date("Y-m-d H:i:s",$t);
echo "<br>set=" . date("Y-m-d H:i:s",$s);

Planrise\Planrise(2014,2,16,26,52,3,'VENUS',$r,$s,$t);
echo "<br>--VENUS--";
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>trans=" . date("Y-m-d H:i:s",$t);
echo "<br>set=" . date("Y-m-d H:i:s",$s);

Planrise\Planrise(2014,2,16,26,52,3,'MARS',$r,$s,$t);
echo "<br>--MARS--";
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>trans=" . date("Y-m-d H:i:s",$t);
echo "<br>set=" . date("Y-m-d H:i:s",$s);

Planrise\Planrise(2014,2,16,26,52,3,'JUPITER',$r,$s,$t);
echo "<br>--JUPITER--";
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>trans=" . date("Y-m-d H:i:s",$t);
echo "<br>set=" . date("Y-m-d H:i:s",$s);

Planrise\Planrise(2014,2,16,26,52,3,'SATURN',$r,$s,$t);
echo "<br>--SATURN--";
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>trans=" . date("Y-m-d H:i:s",$t);
echo "<br>set=" . date("Y-m-d H:i:s",$s);

Planrise\Planrise(2014,2,16,26,52,3,'URANUS',$r,$s,$t);
echo "<br>--URANUS--";
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>trans=" . date("Y-m-d H:i:s",$t);
echo "<br>set=" . date("Y-m-d H:i:s",$s);

Planrise\Planrise(2014,2,16,26,52,3,'NEPTUNE',$r,$s,$t);
echo "<br>--NEPTUNE--";
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>trans=" . date("Y-m-d H:i:s",$t);
echo "<br>set=" . date("Y-m-d H:i:s",$s);

Planrise\Planrise(2014,2,16,26,52,3,'PLUTO',$r,$s,$t);
echo "<br>--PLUTO--";
echo "<br>rise=" . date("Y-m-d H:i:s",$r);
echo "<br>trans=" . date("Y-m-d H:i:s",$t);
echo "<br>set=" . date("Y-m-d H:i:s",$s);


echo "<br><br>Planpos";

Planpos\Planpos(2015,3,9,0,"SUN",$l,$b,$r);
echo "<br>--SUN--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"MERCURY",$l,$b,$r);
echo "<br>--MERCURY--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"VENUS",$l,$b,$r);
echo "<br>--VENUS--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"EARTH",$l,$b,$r);
echo "<br>--EARTH--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"MARS",$l,$b,$r);
echo "<br>--MARS--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"JUPITER",$l,$b,$r);
echo "<br>--JUPITER--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"SATURN",$l,$b,$r);
echo "<br>--SATURN--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"URANUS",$l,$b,$r);
echo "<br>--URANUS--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"NEPTUNE",$l,$b,$r);
echo "<br>--NEPTUNE--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;

Planpos\Planpos(2015,3,9,0,"PLUTO",$l,$b,$r);
echo "<br>--PLUTO--";
echo "<br>l=" . $l;
echo "<br>b=" . $b;
echo "<br>r=" . $r;


echo "<br><br>Phases";

$phases = Phases\Phases(2015);
var_dump ($phases);


echo "<br><br>EclTimer";

$eclipses = EclTimer\EclTimer($phases,-6.8333,33.95);
echo "<br>eclipses = "; var_dump($eclipses);
?>
