<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Write A Review!</title>
		<link rel="stylesheet" href="reviewPage.css">
	</head>
	<body>
	<c:if test="${sessionScope.login == true}">
		<ul>
			<li><b class="navbar-brand" href="index">CICERO</b></li>
			<li class="active"><a href="index">Home</a></li>
			<li><a href="searchPage">Search</a></li>
			<li><a href="logout">Logout</a></li>
			<li><a href="about">About</a></li> 
		</ul>
		<c:if test="${! empty errorMessage}">
				<div class="error">${errorMessage}</div>
			</c:if>
			<h1> Write a review for the TEDTalk</h1>
			<form id="reviewForm" name="reviewForm" action="${pageContext.servletContext.contextPath}/reviewPage" method="post">
				<div>
					<label for="review">Review</label><br>
					<textarea name= "review" id="review" placeholder="Please write a review for the TED Talk" size=12 value ="${model.review}"></textarea>
				</div>
				
				<div class="rating">
				<label for="rating">Rating</label><br>
	 				 <input id="rating1" type="radio" name="rating" value="1">
	 				 <label for="rating1">1</label>
	 				 <input id="rating2" type="radio" name="rating" value="2">
	 				 <label for="rating2">2</label>
	 				 <input id="rating3" type="radio" name="rating" value="3">
	  				 <label for="rating3">3</label>
	 				 <input id="rating4" type="radio" name="rating" value="4">
	  				 <label for="rating4">4</label>
	 				 <input id="rating5" type="radio" name="rating" value="5">
	  				 <label for="rating5">5</label>
				</div>
				
				<button onclick="check()">Submit Review!</button>
		</form>
		<div id ="review"></div>
	</c:if>
	<c:if test="${sessionScope.login != true}">
	<h1>Please login before attempting to write a review.</h1>
	<ul>
			<li><b class="navbar-brand" href="index">TEDTalk Reviews</b></li>
			<li class="active"><a href="index">Home</a></li>
			<li><a href="searchPage">Search</a></li>
			<li><a href="accountCreation">Create Account</a></li>
			<li><a href="login">Login</a></li>
			<li><a href="about">About</a></li> 
		</ul>
	</c:if>
		<script> 
		function check(){
			if(document.reviewForm.review.value == "" || document.reviewForm.rating.value == ""){
				alert("Complete all required fields");
			}
			else{
				alert("Your review was submitted!");
			}
		}
		</script>
	</body>
</html>