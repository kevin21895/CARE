<?php 
$con=mysql_connect("148.72.232.170","kevindoshi95","Ramani95");
		mysql_select_db("Care",$con);
if($con)
{

}
$emparray = array();
$emailid = $_POST["emaild"];
$sql = "Select * FROM Child WHERE emailid = '$emailid' || phoneno = '$emailid'";
$result = mysql_query($sql,$con);
if($result)
{
while($row = mysql_fetch_assoc($result))
{
$emparray ['Child'][] = $row;

}
echo json_encode($emparray);
}
?>