<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="<c:url value="/static/js/add.js" />"></script> 
<link href="<c:url value="/static/css/general.css" />" rel="stylesheet">
<link href="<c:url value="/static/css/choose.css" />" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result mode</title>
</head>
<body>
	<p>User in session: ${logedUserId}</p>
	<h1>Result Mode</h1>
	
	<p>Test duration: ${time}</p>
	<p>Results: </p><br>
     <c:forEach items="${resultsList}" var="element">
			<p>"${element}"</p>
	</c:forEach>
	<br>
	<p>Number of right answers: "${rightAnswers}"</p>
	<p>Number of wrong answers: "${wrongAnswers}"</p>

<ul>
<li><a href="Personal_result_view">Try again</a></li>

</ul>	
	 
</body>
</html>