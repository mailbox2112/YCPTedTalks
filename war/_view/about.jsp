<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>About</title>
		<link rel="stylesheet" href="about.css">
		</head>
		<body>
		<c:if test="${sessionScope.login != true}">
		<ul>
		<li><b href="index">CICERO</b></li>
		<li class="active"><a href="index">Home</a></li>
		<li><a href="searchPage">Search</a></li>
		<li><a href="accountCreation">Create Account</a></li>
		<li><a href="login">Login</a></li>
		<li><a href="about">About</a></li> 
	</ul>
	</c:if>
	<c:if test="${sessionScope.login == true}">
	<ul>
		<li><b href="index">CICERO</b></li>
		<li class="active"><a href="index">Home</a></li>
		<li><a href="searchPage">Search</a></li>
		<li><a href="tedTalkPage">Begin New TedTalk</a></li>
		<li><a href="logout">Logout</a></li>
		<li><a href="about">About</a></li> 
	</ul>
	</c:if>
		<h1>"Never stop learning,<br>
		 because life never <br>
		 stops teaching."</h1>
		<p>CICERO is a website soon to be utilized by the <br>Civil Engineering Department at the York College of Pennsylvania.<br> Redesigned by Aaron Roby, Terell Clark, and Chihea
		Locke, this website allows users to create <br>forum-like posts on their favorite TEDTalks and review them. Based on the idea of eclecticism,<br>  his site is meant for sharing valuable opinions and gaining 
		insight on new things not only from the videos,<br> but from the whole community. </p>
		
	
		
	</body>
</html>
