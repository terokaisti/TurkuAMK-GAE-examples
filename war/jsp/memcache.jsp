<%@page import="net.sf.jsr107cache.CacheException"%>
<%@page import="java.util.Collections"%>
<%@page import="net.sf.jsr107cache.CacheManager"%>
<%@page import="net.sf.jsr107cache.Cache"%>
<%@page import="com.google.appengine.api.quota.QuotaServiceFactory"%>
<%@page import="com.google.appengine.api.quota.QuotaService"%>
<%@ page contentType="text/plain;charset=UTF-8" language="java" %>
<%@ include file="urlfetch.jsp" %>
<%
// http://code.google.com/appengine/docs/quotas.html

Cache cache = null;
try {
    cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
} catch (CacheException e) {
    // ...
}

QuotaService qs = QuotaServiceFactory.getQuotaService();

long start = qs.getCpuTimeInMegaCycles();
String rssCached = (String) cache.get("rss");
if(rssCached == null)
	cache.put("rss", doFetch()); // fetch rss
long end = qs.getCpuTimeInMegaCycles();
double cpuSeconds = qs.convertMegacyclesToCpuSeconds(end - start)*1000;

long start2 = qs.getCpuTimeInMegaCycles();
String rssNoCache = doFetch();
long end2 = qs.getCpuTimeInMegaCycles();
double cpuSeconds2 = qs.convertMegacyclesToCpuSeconds(end2 - start2)*1000;

%>
Cache version:
<%=rssCached!=null?"(no-cache)":rssCached%>
<%=cpuSeconds%>ms cpuseconds used with cache

NoCache (original) version:
<%=rssNoCache%>
<%=cpuSeconds2%>ms cpuseconds used without cache
