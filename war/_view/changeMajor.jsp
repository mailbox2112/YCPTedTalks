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
		<c:if test="${sessionScope.admin != true}">
			<form
				action="${pageContext.servletContext.contextPath}/accountManagement"
				method="post">
				<table>
					<tr>
						<td class="label">Old Major:</td>
						<td><input type="text" name="oldMajor" size="12" /></td>
					</tr>
					<tr>
						<td class="label">New Major:</td>
						<td><input type="text" name="major" size="12" /></td>
					</tr>
					<tr>
						<td class="label">Re-enter New Major:</td>
						<td><input type="text" name="reenteredMajor" size="12" /></td>
					</tr>
					<tr>
						<td />
						<td><input type="Submit" name="submit" value="Change Major"></td>
					</tr>
				</table>
			</form>
		</c:if>
	</c:if>
</body>
</html>