<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link
	href='<c:url value="/static/css_folder/css/CreateQuestionPage.css"></c:url>'
	rel="stylesheet">
<style type="text/css">
*{
text-align: center;
}
a {
	font-size: 1.35em;
	color: blue;
}
a:HOVER {
	color: orange;
}
input:HOVER {
	background-color: yellow;
}
p{
	color: blue;
	border-bottom: 0.01em solid black;		
}
table{
color:white;
background-color: grey;
}
textarea:HOVER {
	background-color: silver;
}
</style>
<title>ADDING</title>
</head>
<body>		
    <p>Creating new Question </p>
	<form action="add_actions">
	
	<table>
	<tr>
	    <td>Please input Question text</td><td>Please input Description text</td>
	</tr>
	<tr>
	    <td><textarea name="questionText"></textarea></td>
	    <td><textarea name="descriptionText"></textarea></td>
    </tr>
    <tr>
        <td>Insert  Category</td>
        <td>Select Level</td>
     </tr>    
	 <tr>
	     <td>		 
		     <input	type="text" name="category" value="C#,Java,HTML" /><br>
		 </td>
		 <td>
			 1<input type="radio" name="question_level" value=1 checked="checked">
			 2<input type="radio" name="question_level" value=2>
			 3<input type="radio" name="question_level" value=3>
			 4<input type="radio" name="question_level" value=4>
			 5<input type="radio" name="question_level" value=5>		
	     </td>
	 </tr>
	 <tr>
	    <td>Input Answer to this Question</td><td>Input number a right question answer</td>
     </tr>
	 <tr>
	    <td>
	        Answer 1 <textarea name="answer_text_1"></textarea><br>
		    Answer 2 <textarea name="answer_text_2"></textarea><br>
		    Answer 3 <textarea name="answer_text_3"></textarea><br>
		    Answer 4 <textarea name="answer_text_4"></textarea><br>
	   </td>
	   <td> <input type="text" name="trueAnswerNumber"	value="1"></td>	          
	</tr>
	<tr><td colspan="2"><input type="submit" value="Add To DataBase"></td></tr>
	</table>	
	</form>
	<br>
	
	<script type="text/javascript">
		document.write("${result}");
	</script>
	<a class="myButton" href='http://localhost:8080/TestsProjectFes/'>...</a>	
</body>
</html>
