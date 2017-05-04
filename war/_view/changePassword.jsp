<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<title>Account Creation</title>
<link rel="stylesheet" type="text/css" href="accountManagement.css">
</head>
<body>
	<c:if test="${sessionScope.login != true}">
		<ul>
			<li><b href="index">Ted Talk Reviews</b></li>
			<li class="active"><a href="index">Home</a></li>
			<li><a href="searchPage">Search</a></li>
			<li><a href="login">Login</a></li>
			<li><a href="about">About</a></li>
		</ul>
		<h1>Please log in before attempting to manage an account!</h1>
	</c:if>

	<c:if test="${sessionScope.login == true}">
		<ul>
			<li><b href="index">Ted Talk Reviews</b></li>
			<li class="active"><a href="index">Home</a></li>
			<li><a href="searchPage">Search</a></li>
			<li><a href="logout">Logout</a></li>
			<li><a href="about">About</a></li>
		</ul>
		<h1>Account Management</h1>
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
		<form
			action="${pageContext.servletContext.contextPath}/accountManagement"
			method="post">
			<table>
				<tr>
					<td class="label">Old Password:</td>
					<td><input type="password" name="oldPassword" size="12" /></td>
				</tr>
				<tr>
					<td class="label">New Password:</td>
					<td><input type="password" name="password" size="12"
						value="${model.password}" /></td>
				</tr>
				<tr>
					<td class="label">Re-enter New Password:</td>
					<td><input type="password" name="reenteredPassword" size="12" /></td>
				</tr>
				<tr>
					<td />
					<td><input type="Submit" name="submit" value="Change Password"></td>
				</tr>
			</table>
		</form>
	</c:if>
</body>
</html>