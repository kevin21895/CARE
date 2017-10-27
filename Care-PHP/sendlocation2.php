<?php
  
  $con=mysql_connect("148.72.232.170","kevindoshi95","Ramani95");
		mysql_select_db("Care",$con);
if($con)
{
echo "Connect";
}
   $emailid = $_POST["puniqueid"];
   $lat = $_POST["lat1"];
  $lon =$_POST["lon1"];;
$message = "location".$lat ."!" . $lon;
   $registatoin_ids = array();
   $sql = "SELECT token FROM Parent WHERE emailid = '$emailid'";
   $result = mysql_query($sql, $con);
   while($row = mysql_fetch_assoc($result)){
    array_push($registatoin_ids, $row['token']);
   }
 
   // Set POST variables
         $url = 'https://fcm.googleapis.com/fcm/send';
   
    $message1 = array("message" => $message);
         $fields = array(
             'registration_ids' => $registatoin_ids,
             'data' => $message1,
         );
   
         $headers = array(
             'Authorization: key=AAAAuxnRIRQ:APA91bG_IimU0WAEvV5ODq0tlgp5lLu8_RXQkhHWUXF_a-FyRnEb5y5e1UCVkcPmxIiV_CkG7nd3u76deXCOlszUYyJY_8brEoMb1NcJgnnN1cqUCOPEEvXe_dGG6NgeOMi_ZQM2GTTx',
             'Content-Type: application/json'
         );
         // Open connection
         $ch = curl_init();
   
         // Set the url, number of POST vars, POST data
         curl_setopt($ch, CURLOPT_URL, $url);
   
         curl_setopt($ch, CURLOPT_POST, true);
         curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
         curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
   
         // Disabling SSL Certificate support temporarly
         curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
   
         curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
   
         // Execute post
         $result = curl_exec($ch);
         if ($result === FALSE) {
             die('Curl failed: ' . curl_error($ch));
         }
   
         // Close connection
         curl_close($ch);
         echo $result;
  
 ?>