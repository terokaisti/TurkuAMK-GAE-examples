<%@page import="com.sun.syndication.feed.synd.SyndEntry"%>
<%@page import="fi.turkuamk.examples.services.URLFetch"%>
<%@ page contentType="text/plain;charset=UTF-8" language="java" %>

<%!
public String doFetch() {
	String all = "";
	URLFetch fetch = new URLFetch();
	for( SyndEntry s : fetch.readRss("http://twitter.com/statuses/user_timeline/16589206.rss") ) {
		String t = s.getTitle();
		all += t;
	}
	return all;
}

%>

<%=doFetch()%>