<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	Register page
	<form action="user/register" method="post">
	<input type="text" name="email"/>
	<input type="text" name="password"/>
	<button type="submit" >Submit</button>
	</form>
	<h1>Upload a File</h1>
    <form action="upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" required />
        <button type="submit">Upload</button>
    </form>
</body>
</html>