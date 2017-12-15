<?php
	
	$con=mysqli_connect("localhost","root","","db");
	
	if(mysqli_connect_errno($con))
	{
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
	
	mysqli_set_charset($con,"utf8");





	
	$res = mysqli_query($con,"select * from digest ");
	
	$result = array();
	
	
	while($row = mysqli_fetch_array($res)){
	
	array_push($result,
		array('phonenumber'=>$row[1], 
		'date' => $row[2],'peecount' => $row[3], 'shitcount'=> $row[4],'totalcount'=>$row[5] 		));
	}
	
	header('Content-Type: application/json; charset=utf8');
	echo json_encode(array("result"=>$result),JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	
	mysqli_close($con);
	?>