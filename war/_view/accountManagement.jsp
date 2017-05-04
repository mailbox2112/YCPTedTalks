<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<head>
<title>Account Creation</title>
<link rel="stylesheet" type="text/css" href="accountCreation.css">
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
	<c:if test="${! empty errorMessage}">
		<div class="error">${errorMessage}</div>
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
		<form
			action="${pageContext.servletContext.contextPath}/accountManagement"
			method="post">
			<c:if test="${sessionScope.no_pw_match != null}">
				<p>Passwords do not match!</p>
			</c:if>
			<table>
				<tr>
					<td><a href="accountManagement?cid=password">Change
							Password</a></td>
				</tr>
				<tr>
					<td><a href="accountManagement?cid=email">Change Email</a></td>
				</tr>
				<c:if test="${sessionScope.admin != true}">
					<tr>
						<td><a href="accountManagement?cid=major">Change Major</a></td>
					</tr>
				</c:if>
			</table>
		</form>
	</c:if>
</body>
</html>