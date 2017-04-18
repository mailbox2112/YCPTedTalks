<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<title>Search List</title>
</head>
<body>
<form action="${pageContext.servletContext.contextPath}/searchView" method="post">
	<table>
		<tr>
			<td><b>Results</b></td>
		</tr>
		<% int count = 0;
		String color = "#F9EBB3";
			if(request.getAttribute("search") != null){
				ArrayList list = (ArrayList) request.getAttribute("search");
				Iterator itr = list.iterator();
				while (itr.hasNext()){
					  if ((count % 2) == 0) {
                          color = "#eeffee";
                      }
				}
				count++;
                ArrayList sList = (ArrayList) itr.next();
			%>
			<tr>
			<td><%=sList.get(0)%></td>
			</tr>
			<%
                    }
                if (count == 0) {
            %>
            <tr>
                <td colspan=4 align="center"
                    style="background-color:#eeffee"><b>No Record Found..</b></td>
            </tr>
            <% }
            %>
	</table>
	 </form>
</body>
</html>