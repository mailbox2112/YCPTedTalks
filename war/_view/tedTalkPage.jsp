<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
	<title>Ted Talk </title>
	<link rel="stylesheet" href="tedTalkPage.css">
</head>
<body>
<form id="reviewForm" name="reviewForm" action="${pageContext.servletContext.contextPath}/tedTalkPage" method="post">
	<h1><b>${talk.title}</b></h1>
	<b>${talk.author}</b>
	<a>${talk.link}</a>
	<br>
	<em>${talk.descript}</em>
	<em>${talk.topic}</em>
	
	<span class="reviews">
		<b>${talk.reviews}</b>
	</span>
	
	<span class ="ratings">
	</span>
	
	<span class ="recommendations_box">
	</span>
	
	<input name="reviewPage" type="submit" value="Write A Review" />
</form>
</body>
</html>