<?php
	
	$con=mysqli_connect("localhost","root","","db");
	
	if(mysqli_connect_errno($con))
	{
		echo "Failed to connect to MySQL: " . mysqli_connect_error();
	}
	
	mysqli_set_charset($con,"utf8");
	
	$res = mysqli_query($con,"select * from Profile");
	
	$result = array();
	
	
	while($row = mysqli_fetch_array($res)){
	
	array_push($result,
		array('phonenumber'=>$row[0], 'nickname'=>$row[1],'name'=>$row[2],'age'=>$row[3],'sex' =>$row[4]
		));
	}
	
	
	
	header('Content-Type: application/json; charset=utf8');
	echo json_encode(array("result"=>$result),JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
	
	mysqli_close($con);
	?>