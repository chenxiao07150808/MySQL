# MySQL
                                 简单是实现android与php mysql数据库的连接。
这里提供的代码只是为了使你能简单的连接 Android 项目和 PHP，MySQL。你不能把它作为一个标准或者安全编程实践。在生产环境中，理想情况下你需要避免使用任何可能造成潜在注入漏洞的代码（比如 MYSQL 注入）。

涉及的PHP文件
db_connect.php
<?php
/**
* A class file to connect to database
*/
classDB_CONNECT {
// constructor
function__construct() {
// connecting to database
$this->connect();
}
// destructor
function__destruct() {
// closing db connection
$this->close();
}
/**
* Function to connect with database
*/
functionconnect() {
// import database connection variables
require_once__DIR__ . '/db_config.php';
// Connecting to mysql database
$con= mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) ordie(mysql_error());
// Selecing database
$db= mysql_select_db(DB_DATABASE) ordie(mysql_error()) ordie(mysql_error());
// returing connection cursor
return$con;
}
/**
* Function to close db connection
*/
functionclose() {
// closing db connection
mysql_close();
}
}
?>
怎么调用 ：当你想连接 MySQl 数据库或者执行某些操作时，可以这样使用 db_connect.php
$db= newDB_CONNECT(); // creating class object(will open database connection)
create_product.php
<?php
02.
03./*
04. * Following code will create a new product row
05. * All product details are read from HTTP Post Request
06. */
07.
08.// array for JSON response
09.$response = array();
10.
11.// check for required fields
12.if (isset($_POST['name']) && isset($_POST['price']) && isset($_POST['description'])) {
13.
14. $name = $_POST['name'];
15. $price = $_POST['price'];
16. $description = $_POST['description'];
17.
18. // include db connect class
19. require_once __DIR__ . '/db_connect.php';
20.
21. // connecting to db
22. $db = new DB_CONNECT();
23.
24. // mysql inserting a new row
25. $result = mysql_query("INSERT INTO products(name, price, description) VALUES('$name', '$price',
'$description')");
26.
27. // check if row inserted or not
28. if ($result) {
29. // successfully inserted into database
30. $response["success"] = 1; 
31. $response["message"] = "Product successfully created.";
32.
33. // echoing JSON response
34. echo json_encode($response);
35. } else {
36. // failed to insert row
37. $response["success"] = 0;
38. $response["message"] = "Oops! An error occurred.";
39.
40. // echoing JSON response
41. echo json_encode($response);
42. }
43.} else {
44. // required field is missing
45. $response["success"] = 0;
46. $response["message"] = "Required field(s) is missing";
47.
48. // echoing JSON response
49. echo json_encode($response);
50.}
51.?>

对于上面的代码，JSON 的返回值会是：
当 POST 参数丢失
{
"success": 0,
"message": "Required field(s) is missing"
}
当 product 成功创建
{
"success": 1,
"message": "Product successfully created."
}
当插入数据时出现错误
{
"success": 0,
"message": "Oops! An error occurred."
}
get_product_details.php
<?php
/*
* Following code will get single product details
* A product is identified by product id (pid)
*/
// array for JSON response
$response= array();
// include db connect class
require_once__DIR__ . '/db_connect.php';
// connecting to db
$db= newDB_CONNECT();
// check for post data
if(isset($_GET["pid"])) {
$pid= $_GET['pid'];
// get a product from products table
$result= mysql_query("SELECT *FROM products WHERE pid = $pid");
if(!empty($result)) {
// check for empty result
if(mysql_num_rows($result) > 0) {
$result= mysql_fetch_array($result);
$product= array();
$product["pid"] = $result["pid"];
$product["name"] = $result["name"];
$product["price"] = $result["price"];
$product["description"] = $result["description"];
$product["created_at"] = $result["created_at"];
$product["updated_at"] = $result["updated_at"];
// success
$response["success"] = 1;
// user node
$response["product"] = array();
array_push($response["product"], $product);
// echoing JSON response
echojson_encode($response);
} else{
// no product found
$response["success"] = 0;
$response["message"] = "No product found";
// echo no users JSON
echojson_encode($response);
}
} else{
// no product found
$response["success"] = 0;
$response["message"] = "No product found";
// echo no users JSON
echojson_encode($response);
}
} else{ 
// required field is missing
$response["success"] = 0;
$response["message"] = "Required field(s) is missing";
// echoing JSON response
echojson_encode($response);
}
?>
The json response for the above file will be
When successfully getting product details
{
"success": 1,
"product": [
{
"pid": "1",
"name": "iPHone 4S",
"price": "300.00",
"description": "iPhone 4S white",
"created_at": "2012-04-29 01:41:42",
"updated_at": "0000-00-00 00:00:00"
}
]
}
When no product found with matched pid
{
"success": 0,
"message": "No product found"
}
get_all_products.php
<?php
/*
* Following code will list all the products
*/
// array for JSON response
$response= array();
// include db connect class
require_once__DIR__ . '/db_connect.php';
// connecting to db
$db= newDB_CONNECT();
// get all products from products table
$result= mysql_query("SELECT *FROM products") ordie(mysql_error()); 
// check for empty result
if(mysql_num_rows($result) > 0) {
// looping through all results
// products node
$response["products"] = array();
while($row= mysql_fetch_array($result)) {
// temp user array
$product= array();
$product["pid"] = $row["pid"];
$product["name"] = $row["name"];
$product["price"] = $row["price"];
$product["created_at"] = $row["created_at"];
$product["updated_at"] = $row["updated_at"];
// push single product into final response array
array_push($response["products"], $product);
}
// success
$response["success"] = 1;
// echoing JSON response
echojson_encode($response);
} else{
// no products found
$response["success"] = 0;
$response["message"] = "No products found";
// echo no users JSON
echojson_encode($response);
}
?>
And the JSON response for above code
Listing all Products
{
"products": [
{
"pid": "1",
"name": "iPhone 4S",
"price": "300.00",
"created_at": "2012-04-29 02:04:02",
"updated_at": "0000-00-00 00:00:00"
},
{
"pid": "2",
"name": "Macbook Pro",
"price": "600.00",
"created_at": "2012-04-29 02:04:51",
"updated_at": "0000-00-00 00:00:00"
},
{
"pid": "3",
"name": "Macbook Air",
"price": "800.00",
"created_at": "2012-04-29 02:05:57",
"updated_at": "0000-00-00 00:00:00"
},
{ 
"pid": "4",
"name": "OS X Lion",
"price": "100.00",
"created_at": "2012-04-29 02:07:14",
"updated_at": "0000-00-00 00:00:00"
}
],
"success": 1
}
When products not found
{
"success": 0,
"message": "No products found"
}

update_product.php
<?php
/*
* Following code will update a product information
* A product is identified by product id (pid)
*/
// array for JSON response
$response= array();
// check for required fields
if(isset($_POST['pid']) && isset($_POST['name']) && isset($_POST['price']) && isset($_POST['description'])) {
$pid= $_POST['pid'];
$name= $_POST['name'];
$price= $_POST['price'];
$description= $_POST['description'];
// include db connect class
require_once__DIR__ . '/db_connect.php';
// connecting to db
$db= newDB_CONNECT();
// mysql update row with matched pid
$result= mysql_query("UPDATE products SET name = '$name', price = '$price', description = '$description'
WHERE pid = $pid");
// check if row inserted or not
if($result) {
// successfully updated
$response["success"] = 1;
$response["message"] = "Product successfully updated.";
// echoing JSON response
echojson_encode($response);
} else{
}
} else{ 
// required field is missing
$response["success"] = 0;
$response["message"] = "Required field(s) is missing";
// echoing JSON response
echojson_encode($response);
}
?>
The json reponse of above code, when product is updated successfully
{
"success": 1,
"message": "Product successfully updated."
}


delete_product.php
<?php
/*
* Following code will delete a product from table
* A product is identified by product id (pid)
*/
// array for JSON response
$response= array();
// check for required fields
if(isset($_POST['pid'])) {
$pid= $_POST['pid'];
// include db connect class
require_once__DIR__ . '/db_connect.php';
// connecting to db
$db= newDB_CONNECT();
// mysql update row with matched pid
$result= mysql_query("DELETE FROM products WHERE pid = $pid");
// check if row deleted or not
if(mysql_affected_rows() > 0) {
// successfully updated
$response["success"] = 1;
$response["message"] = "Product successfully deleted";
// echoing JSON response
echojson_encode($response);
} else{
// no product found
$response["success"] = 0;
$response["message"] = "No product found";
// echo no users JSON
echojson_encode($response); 
}
} else{
// required field is missing
$response["success"] = 0;
$response["message"] = "Required field(s) is missing";
// echoing JSON response
echojson_encode($response);
}
?>
When product successfully deleted
{
"success": 1,
"message": "Product successfully deleted"
}
When product not found
{
"success": 0,
"message": "No product found"
}
