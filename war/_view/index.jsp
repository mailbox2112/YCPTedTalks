<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Index</title>
		<h1> TEDTalk Reviews </h1>
		 <link rel="stylesheet" type="text/css" href="indexPage.css">
	</head>

	<body>
	<% String name=(String)session.getAttribute("name");
		if (name != null) {%> <h1>Welcome, ${name}!</h1><%}%>
		<form action="${pageContext.servletContext.contextPath}/index" method="post">
	<%Boolean login=(Boolean)session.getAttribute("login");
		if (login != null){ %><input name="logout" type="submit" value="Logout" />
			<input name="reviewPage" type="submit" value="Write A Review" />
			<input name="searchPage" type="submit" value="Search"/><%} 
		else { %>
			<input name="login" type="submit" value="Login" />
			<input name="createAccount" type="submit" value="Create An Account" /><%} %>
		</form>
		<br>
	<div class="popular_reviews">
		<h2>Popular Ted Talks</h2>
	</div>
	</body>
</html>
