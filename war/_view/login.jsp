<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Login</title>
		<style type="text/css">
		.error {
			color: red;
		}
		
		td.label {
			text-align: right;
		}
		input[type=text]{
			box-sizing: border-box;
			border: 2px solid #ff193d;
 			font-size: 16px;
 			font-family: Helvetica Neue;
		}
		input[type=password]{
			box-sizing: border-box;
			border: 2px solid #ff193d;
 			font-size: 16px;
 			font-family: Helvetica Neue;
		}
		
		input[type=submit]{
   		 background-color: white; 
   		 color: black; 
   		 border: 2px solid #E62B1E;
   		 padding: 6px 20px;
   		 text-align: center;
   		 text-decoration: none;
   		 display: inline-block;
    		 font-size: 16px;
   		 margin: 4px 2px;
   		 -webkit-transition-duration: 0.4s; /* for Safari */
    		 transition-duration: 0.4s;
   		 cursor: pointer;
		}

		input[type=submit]:hover{
			background-color: #E62B1E;
   		    color: white;
		}
		</style>
	</head>

	<body>
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
		<form action="${pageContext.servletContext.contextPath}/login" method="post">
			<%Boolean invalidLogin = (Boolean)request.getSession().getAttribute("login_failure"); 
				if(invalidLogin != null) {%><p>Invalid user name and/or password</p><%} %>
			<table>
				<tr>
					<td class="label">Email:</td>
					<td><input type="text" name="email" size="12" value="${model.email}" /></td>
				</tr>
				<tr>
					<td class="label">Password:</td>
					<td><input type="password" name="password" size="12" value="${model.password}" /></td>
				</tr>
			</table>
			<input type="Submit" name="submit" value="Login">
		</form>
	</body>
</html>