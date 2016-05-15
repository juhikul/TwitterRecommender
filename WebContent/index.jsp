<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.twitterRecommender.datamodel.TimeFinderResponse,com.twitterRecommender.utils.Constants,twitter4j.User"%>
<!DOCTYPE html>
<!--  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tag Recommender</title>
</head>
<body>
	
	<form name="form" action="Tagger.do" method="post">
	
	Enter user id here :
	<input type="text" name="userId" onkeypress='return event.charCode >= 48 && event.charCode <= 57'></input>

	
	<br>
	<br>
	<input type="submit" value="GO !"> 
	</form>
	
	
	<%TimeFinderResponse res =  (TimeFinderResponse) request.getAttribute("bestTime");
		if (res!=null) {
			int hour = res.getBestTime().getTimeInHour();
			int week = res.getBestTime().getTimeInWeek();
			User user = res.getUser();
	%>
	<p> User Id : <%=user.getId() %> </p>
	<p> User Name : <%=user.getScreenName() %> </p>

	<p> Best Hour to Post : 
	
	<% if (hour==-1) { %>
		Could not understand best time for posting.
	<% } else { %>
		<%=hour %><sup>th</sup> to <%=hour+1 %><sup>th</sup> hour
	<% } %>
 	</p>

	<p> Best Day to Post : 
	
	<% 
		String weekTime = null;
		if (week==-1) {
			weekTime = "Could not understand best time for posting.";
		} else { 
			switch (week) {
			case 0: weekTime = "Sunday";
				break;
			case 1: weekTime = "Monday";
				break;
			case 2: weekTime = "Tuesday";
				break;
			case 3: weekTime = "Wednesday";
				break;
			case 4: weekTime = "Thursday";
				break;
			case 5: weekTime = "Friday";
				break;
			case 6: weekTime = "Saturday";
				break;
			}
	%>
		<%=weekTime %>
	<% } %>
 	</p>
	<%	}

	%>
	<%String status =  (String) request.getAttribute("status");
		if (status!=null && Constants.INVALID_USER.equalsIgnoreCase(status)) {
	%>	
	<%="Invalid user. Enter correct user id" %>
	<%	}
	%>
</body>
</html>