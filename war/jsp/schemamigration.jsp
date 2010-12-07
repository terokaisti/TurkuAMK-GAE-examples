<%@page import="fi.turkuamk.examples.taskqueue.Person2"%>
<%@page import="fi.turkuamk.examples.taskqueue.Person1"%>
<%@page import="com.google.code.twig.annotation.AnnotationObjectDatastore"%>
<%@page import="com.google.code.twig.ObjectDatastore"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html><head></head><body>

<h1>Person1:</h1>
<%
ObjectDatastore ds = new AnnotationObjectDatastore();
for( Person1 p : ds.find().type(Person1.class).returnAll().now() ) {
%>
<li/><%=p.name%>
<%
}
%>
<h1>Person2:</h1>
<%
for( Person2 p : ds.find().type(Person2.class).returnAll().now() ) {
%>
<li/><%=p.firstName+" "+p.lastName%>
<%
}
%>

<hr/>

<input type="button" value="Migrate Person1 -&gt; Person2" 
onclick="location.href='/worker/migrate?to=p2'"
/>
<input type="button" value="Migrate Person2 -&gt; Person1" 
onclick="location.href='/worker/migrate?to=p1'"
/>

<hr/>
<h1>Add Person1</h1>(firstname lastname)
<% 
String name = request.getParameter("name");
if(name != null) {
	Person1 p = new Person1();
	p.name = name;
	ds.store(p);
}
%>
<form method="post">
	<input type="text" name="name"/>
	<input type="submit" />
</form>
</body></html>