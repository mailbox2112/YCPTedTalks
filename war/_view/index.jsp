<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Index</title>
	</head>

	<body>
		<form action="${pageContext.servletContext.contextPath}/index" method="post">
			<input name="addNumbers" type="submit" value="Add Numbers" />
			<input name="multiplyNumbers" type="submit" value="Multiply Numbers" />
			<input name="guessingGame" type="submit" value="Guessing Game" />
			<input name="login" type="submit" value="Login" />
			<input name="reviewPage" type="submit" value="Write A Review" />
			<input name="ReadAReview" type="submit" value="Review Page" />
		</form>
	</body>
</html>
