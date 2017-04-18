<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
	<head>
		<title>Search</title>
		<link rel="stylesheet" href="searchPage.css">
		</style>
	</head>
<body>
	<form action="${pageContext.servletContext.contextPath}/searchPage" method="post">
		<select id="change_selection">
			<option>Search by Author</option>
			<option>Search by Topic</option>
			<option>Search by Title</option>
		</select>
		<input type="text" id ="search" name="search" size="36" value= "${model.search}"/>
		<input type="Submit" name="submit" value="Search!">
	</form>
	<script>
		
	</script>
</body>
</html>