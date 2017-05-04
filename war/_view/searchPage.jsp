<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.util.List" %>
<% //Source: https://www.aspsnippets.com/Articles/Show-Hide-DIV-with-TextBox-based-on-DropDownList-Selected-Value-Selection-using-JavaScript-and-jQuery.aspx %>
<html>
	<head>
		<title>Search</title>
		<link rel="stylesheet" href="searchPage.css">
	</head>
<body>
	<c:if test="${sessionScope.error == true}">
		<h1>Please type in your search criteria.</h1>
	</c:if>
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
	
	<form action="${pageContext.servletContext.contextPath}/searchPage" method="post">
	<h1>Search TEDTalks</h1>
		<select name="options" id="Type">
			<option value="author"> Search by Reviewer</option>
			<option value="topic"> Search by Topic</option>
			<option value="title">Search by Title</option>
		</select>
		<input type="text" id ="search" name="search" size="36" value= "${model.search}"/>
		<input type="text" id="extraSearch" name="extraSearch" size="36" value="${model.extraSearch}"/>
		<input type="Submit" name="submit" value="Search!">
	</form>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
	<script type="text/javascript">
		$(function () {
			$("#Type").change(function() {
				if($(this).val() == "author") {
					$("#extraSearch").show();
				} else {
					$("#extraSearch").hide();
				}
			});
		});
	</script>
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js">
		$(document).ready(function() {
			$("#selectReview").click(function handle(id) {
				console.log(id);
				$.ajax({
					type: "POST",
					url: "/aroby/tedTalkView",
					data: {reviewPage: id},
					dataType: "text"
				});
			});
		});
	</script>
</body>
</html>
