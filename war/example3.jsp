<%@page import="java.util.concurrent.Future"%>
<%@page import="fi.turkuamk.examples.example3.UserEntry"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.code.twig.ObjectDatastore" %>
<%@ page import="com.google.code.twig.annotation.AnnotationObjectDatastore" %>
<% ObjectDatastore ds = new AnnotationObjectDatastore(); 
Future<Integer> userCount = ds.find().type(UserEntry.class).returnCount().later();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Example3</title>
</head>
<body>

<fieldset><legend>Tallenna käyttäjä</legend>
<form method="post" action="/example3"><input type="hidden"
	name="action" value="store" /> Etunimi: <input type="text"
	name="firstName" /><br />
Sukunimi: <input type="text" name="lastName" /><br />
<input type="submit" /></form>
<% if( request.getAttribute("action") != null && request.getParameter("action").equals("store") ) { %>
Käyttäjä tallennettu. <% } %>
</fieldset>

Käyttäjiä tietokannassa: <%= userCount.get() %>



</body>
</html>