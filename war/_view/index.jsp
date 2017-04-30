<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Index</title>
<link rel="stylesheet" type="text/css" href="indexPage.css">
	<body>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>

	<form action="${pageContext.servletContext.contextPath}/index"
		method="post">
		<c:choose>
			<c:when test="${sessionScope.login == true}">
				<nav class="navbar navbar-inverse">
					<div class="container-fluid">
						<div class="navbar-header">
							<ul class="nav navbar-nav">
								<li><b href="index">Ted Talk Reviews</b></li>
								<li class="active"><a href="index">Home</a></li>
								<li><a href="searchPage">Search</a></li>
								<li><a href="tedTalkPage">Begin New TedTalk</a></li>
								<li><a href="logout">Logout</a></li>
								<li><a href="about">About</a></li>
								<li><b class="welcome">Welcome, ${sessionScope.name}!</b></li>
							</ul>
						</div>
					</div>
				</nav>
			</c:when>
			<c:when test="${sessionScope.login != true}">
				<nav class="navbar navbar-inverse">
					<div class="container-fluid">
						<div class="navbar-header">
							<ul class="nav navbar-nav">
								<li><b class="navbar-brand" href="index">Ted Talk
										Reviews</b></li>
								<li class="active"><a href="index">Home</a></li>
								<li><a href="searchPage">Search</a></li>
								<li><a href="login">Login</a></li>
								<li><a href="about">About</a></li>
							</ul>
						</div>
					</div>
				</nav>
			</c:when>
		</c:choose>


		<h1>TEDTalk Reviews</h1>
		<p>Opinions worth sharing</p>
		<div class="recent_reviews">
			<h2>Recent Ted Talks Reviews</h2>
			<c:if test="${sessionScope.results == false}">
				<h2>nothing</h2>
			</c:if>
			<c:if test="${sessionScope.results == true}">
				<table>
					<c:forEach items="${sessionScope.tedTalks}" var="talk">
						<tr>
							<td>Title: <c:out value="${talk.title}" />
							<td>
						</tr>
						<c:forEach items="${sessionScope.accounts}" var="account">
							<c:forEach items="${sessionScope.reviews}" var="review">
								<c:if test="${review.tedTalkId == talk.tedTalkId}">
									<tr>
										<td>Rating: <c:out value="${review.rating}" /></td>
									</tr>
								</c:if>
								<c:if test="${review.accountId == account.accountId}">
									<tr>
										<td>Reviewer: <c:out value="${account.firstName}" /> <c:out
												value="${account.lastName}" /></td>
									</tr>
									<tr>
										<td><a href="searchPage">Search this Reviewer</a>
										<td>
									</tr>
								</c:if>
							</c:forEach>
						</c:forEach>
					</c:forEach>
				</table>

			</c:if>
		</div>
	</form>
</body>
</html>
