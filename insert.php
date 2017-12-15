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
$nickname=isset($_POST['nickname']) ? $_POST['nickname'] : '';  
$name=isset($_POST['name']) ? $_POST['name']:'';
$age=isset($_POST['age']) ? $_POST['age']: '';
$sex=isset($_POST['sex']) ? $_POST['sex'] : '';  

if ($phonenumber !="" and $nickname !="" and $name !="" and $age !="" and $sex !=""){   
  
    $sql="insert into Profile(phonenumber,nickname,name,age,sex) 	 values('$phonenumber','$nickname','$name','$age','$sex')";  
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
         NickName: <input type = "text" name = "nickname" />
         Name: <input type = "text" name = "name"/>
         Age: <input type = "text" name = "age"/>
         Sex: <input type = "text" name = "sex"/>
         <input type = "submit" />
      </form>
   
   </body>
</html>
<?php
}
?>