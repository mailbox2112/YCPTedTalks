<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<title>Login</title>
<link rel="stylesheet" type="text/css" href="loginPage.css">
</head>

<body>
	<c:if test="${! empty errorMessage}">
		<div class="error">${errorMessage}</div>
	</c:if>
	<form action="${pageContext.servletContext.contextPath}/login"
		method="post">
		<c:if test="${sessionScope.login_failure == true}">
			<p>Invalid user name and/or password</p>
		</c:if>
		<div class="header">
			<ul>
				<li><b class="navbar-brand" href="index">Ted Talk Reviews</b></li>
				<li class="active"><a href="index">Home</a></li>
				<li><a href="searchPage">Search</a></li>
				<li><a href="accountCreation">Create Account</a></li>
				<li><a href="login">Login</a></li>
				<li><a href="about">About</a></li>
			</ul>
		</div>
		<div class="loginForm">
			<h1>Login</h1>
			<table>
				<tr>
					<td class="label">Email:</td>
					<td><input type="text" name="email" size="12"
						value="${model.email}" /></td>
				</tr>
				<tr>
					<td class="label">Password:</td>
					<td><input type="password" name="password" size="12"
						value="${model.password}" /></td>
				</tr>
			</table>
			<div class="button">
				<input type="Submit" name="submit" value="Login">
			</div>
		</div>
	</form>
</body>
</html>