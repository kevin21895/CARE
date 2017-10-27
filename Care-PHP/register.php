<?php 
$con=mysql_connect("148.72.232.170","kevindoshi95","Ramani95");
		mysql_select_db("Care",$con);
if($con)
{
echo "Connect";
}
$name =$_POST["name"];
	$emailid =$_POST["emailid"];
	$password =$_POST["password"];
	$phoneno = $_POST["phoneno"];
	$user =$_POST["user"];
	$cuniqueid =$_POST["cuniqueid"];
	
	$token =$_POST["token"];
     $device = $_POST["device"];
	if($user == "Parent")
	{
		
	
	$sql = "Select emailid from Parent where emailid = '$emailid';";
	$result = mysql_query($sql,$con);
	if($result)
	{
	$row = mysql_num_rows($result);
	if($row > 0)
	{
	echo "UserAlreadyRegistered";
	}

	}
	if($row == 0)
	{ 
	$sql1 = "Select emailid from Child where emailid = '$emailid';";
	$res = mysql_query($sql1,$con);
	if($res)
	{
	$row = mysql_num_rows($res);
	if($row > 0)
	{
	echo "UserAlreadyRegistered";
	}
	}
   if($row == 0)
	{
	$sql2 = "INSERT INTO Parent VALUES('$name','$emailid','$password','$phoneno','$cuniqueid','$token','$user');";
	$result = mysql_query($sql2,$con);
	if($result)
	{
		$sql3 = "UPDATE Child SET parent_uniqueid = '$emailid' WHERE emailid = '$cuniqueid' || phoneno = '$cuniqueid';";
		$result = mysql_query($sql3,$con);
		if($result)
		{
			echo "Registered";
		}
		else{
			echo "NotRegistered";
		}
		
	
	}
	else
	{
	echo "ChildProblem";
	}
	}
	}
	}
	else if($user == "Child")
	{
	$sql = "Select emailid from Child where emailid = '$emailid'";
	$result = mysql_query($sql,$con);
	if($result)
	{
	$row = mysql_num_rows($result);
	if($row > 0)
	{
	echo "UserAlreadyRegistered";
	}
	}
	if($row == 0)
	{
	$sql1 = "Select emailid from Parent where emailid = '$emailid'";
	$result = mysql_query($sql1,$con);
	if($result)
	{
	$row = mysql_num_rows($result);
	if($row > 0)
	{
	echo "UserAlreadyRegistered";
	}
	}
	if($row == 0)
	{
	$sql2 = "INSERT INTO Child VALUES('$name','$emailid','$password','$phoneno','$cuniqueid','$token','$user','$device');";
	$result = mysql_query($sql2,$con);
	if($result)
	{
	echo "Registered";
	}
	else
	{
	echo "NotRegistered";
	}
	}
	}
	}
	?>