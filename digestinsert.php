<?php  
error_reporting(E_ALL); 
ini_set('display_errors',1); 

$link=mysqli_connect("localhost","root","","db"); 
if (!$link)  
{ 
   echo "MySQL 접속 에러 : ";
   echo mysqli_connect_error();
   exit();
}  


mysqli_set_charset($link,"utf8");  

//POST 값을 읽어온다.
$phonenumber=isset($_POST['phonenumber']) ? $_POST['phonenumber'] : '';  
$date=isset($_POST['date']) ? $_POST['date'] : '';  
$peecount =	isset($_POST['peecount']) ? $_POST['peecount'] : '';  
$shitcount =	isset($_POST['shitcount']) ? $_POST['shitcount'] : '';  
$totalcount = 	isset($_POST['totalcount']) ? $_POST['totalcount'] : '';  

if ($phonenumber !="" and $date !=""){   
  
    $sql="insert into digest(phonenumber,date,peecount,shitcount,totalcount) 	 values('$phonenumber','$date','$peecount','$shitcount','$totalcount')";  
    $result=mysqli_query($link,$sql);  

    if($result){  
       echo "SQL Success";  
    }  
    else{  
       echo "SQL Error : "; 
       echo mysqli_error($link);
    } 
 
} else {
    echo "Insert Data Please ";
}


mysqli_close($link);
?>

<?php

$android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

if (!$android){
?>

<html>
   <body>
   
      <form action="<?php $_PHP_SELF ?>" method="POST">
        Phonenumber: <input type = "text" name = "phonenumber" />
         Date: <input type = "text" name = "date" />
         Peecount : <input type = "text" name = "peecount"/>
         Shitcount : <input type = "text" name = "shitcount" />
         Totalcount : <input type = "text" name = "totalcount" />
         
         <input type = "submit" />
      </form>
   
   </body>
</html>
<?php
}
?>