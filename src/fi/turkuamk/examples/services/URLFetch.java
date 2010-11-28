package fi.turkuamk.examples.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

public class URLFetch {
	/**
	 * readRss Installation:
	 * 
	 * 1. WEB-INF/lib/xercesImpl.jar
	 * 2. WEB-INF/lib/xml-apis.jar
	 * 3. WEB-INF/lib/jdom.jar (build path)
	 * 4. WEB-INF/lib/rome-1.0.jar (build path)
	 * 
	 * @param urlString RSS
	 * @return
	 */
	public List<SyndEntry> readRss(String urlString) {
		try {
			URL url = new URL(urlString);
			SyndFeedInput input = new SyndFeedInput();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            SyndFeed feed = input.build(reader);
            feed.setEncoding( "ISO-8859-1" );
			List<SyndEntry> entries = feed.getEntries();
			return entries;
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
