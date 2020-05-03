package comp3111.coursescraper;


import org.junit.*;

import comp3111.coursescraper.Scraper;
import comp3111.coursescraper.Course;
import comp3111.coursescraper.Section;
import comp3111.coursescraper.Slot;

import static org.junit.Assert.*;


public class ScrapeText {

	private Scraper s = new Scraper();
	
	@Test
	public void testScrape() {
		assertNotNull(s.scrape("https://w5.ab.ust.hk/wcq/cgi-bin/", "1910", "COMP"));
		assertNull(s.scrape("https://w5.ab.ust.hk/wcq/", "1910", "COMP"));
	}
}
