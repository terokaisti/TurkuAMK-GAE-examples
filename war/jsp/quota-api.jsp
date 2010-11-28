<%@page import="com.google.appengine.api.quota.QuotaServiceFactory"%>
<%@page import="com.google.appengine.api.quota.QuotaService"%>
<%@ page contentType="text/plain;charset=UTF-8" language="java" %>
<%@ include file="urlfetch.jsp" %>
<%
// http://code.google.com/appengine/docs/quotas.html

QuotaService qs = QuotaServiceFactory.getQuotaService();
long start = qs.getCpuTimeInMegaCycles();

doFetch(); // fetch rss

long end = qs.getCpuTimeInMegaCycles();
double cpuSeconds = qs.convertMegacyclesToCpuSeconds(end - start)*1000;

%>
<%=cpuSeconds%>ms cpuseconds used for urlfetch
