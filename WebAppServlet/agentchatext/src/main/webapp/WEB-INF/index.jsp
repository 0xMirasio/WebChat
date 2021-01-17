<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Agent Chat Application</title>
</head>
<body>
	<h1>Agent Chat Application</h1>

	<style>
		body {
		  background-image: url('Images/image.jpg');
		}
	</style>

	<ul>
		<li><a href=/agentchatext/>Home</a></li>
	</ul>

	<p>
		<% 
		String attribut = (String) request.getAttribute("test");
		out.println( attribut );
		%>
	</p>


	
</body>
</html>
