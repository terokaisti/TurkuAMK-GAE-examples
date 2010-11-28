//package fi.turkuamk.examples.tests;
//
//import org.junit.Test;
//
//import com.sun.syndication.feed.synd.SyndEntry;
//
//import fi.turkuamk.examples.services.URLFetch;
//
//public class URLFetchTest {
//	@Test
//	public void testURLFetch() {
//		URLFetch fetch = new URLFetch();
//		for( SyndEntry entry : fetch.readFeed("http://www.iltalehti.fi/rss.xml") ) {
//			System.out.println(entry.getTitle());
//		}
//	}
//}
