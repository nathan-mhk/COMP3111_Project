package comp3111.coursescraper;

import java.net.URLEncoder;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.DomText;
import java.util.Vector;


/**
 * WebScraper provide a sample code that scrape web content. After it is constructed, you can call the method scrape with a keyword, 
 * the client will go to the default url and parse the page by looking at the HTML DOM.  
 * <br>
 * In this particular sample code, it access to HKUST class schedule and quota page (COMP). 
 * <br>
 * https://w5.ab.ust.hk/wcq/cgi-bin/1830/subject/COMP
 *  <br>
 * where 1830 means the third spring term of the academic year 2018-19 and COMP is the course code begins with COMP.
 * <br>
 * Assume you are working on Chrome, paste the url into your browser and press F12 to load the source code of the HTML. You might be freak
 * out if you have never seen a HTML source code before. Keep calm and move on. Press Ctrl-Shift-C (or CMD-Shift-C if you got a mac) and move your
 * mouse cursor around, different part of the HTML code and the corresponding the HTML objects will be highlighted. Explore your HTML page from
 * body &rarr; div id="classes" &rarr; div class="course" &rarr;. You might see something like this:
 * <br>
 * <pre>
 * {@code
 * <div class="course">
 * <div class="courseanchor" style="position: relative; float: left; visibility: hidden; top: -164px;"><a name="COMP1001">&nbsp;</a></div>
 * <div class="courseinfo">
 * <div class="popup attrword"><span class="crseattrword">[3Y10]</span><div class="popupdetail">CC for 3Y 2010 &amp; 2011 cohorts</div></div><div class="popup attrword"><span class="crseattrword">[3Y12]</span><div class="popupdetail">CC for 3Y 2012 cohort</div></div><div class="popup attrword"><span class="crseattrword">[4Y]</span><div class="popupdetail">CC for 4Y 2012 and after</div></div><div class="popup attrword"><span class="crseattrword">[DELI]</span><div class="popupdetail">Mode of Delivery</div></div>	
 *    <div class="courseattr popup">
 * 	    <span style="font-size: 12px; color: #688; font-weight: bold;">COURSE INFO</span>
 * 	    <div class="popupdetail">
 * 	    <table width="400">
 *         <tbody>
 *             <tr><th>ATTRIBUTES</th><td>Common Core (S&amp;T) for 2010 &amp; 2011 3Y programs<br>Common Core (S&amp;T) for 2012 3Y programs<br>Common Core (S&amp;T) for 4Y programs<br>[BLD] Blended learning</td></tr><tr><th>EXCLUSION</th><td>ISOM 2010, any COMP courses of 2000-level or above</td></tr><tr><th>DESCRIPTION</th><td>This course is an introduction to computers and computing tools. It introduces the organization and basic working mechanism of a computer system, including the development of the trend of modern computer system. It covers the fundamentals of computer hardware design and software application development. The course emphasizes the application of the state-of-the-art software tools to solve problems and present solutions via a range of skills related to multimedia and internet computing tools such as internet, e-mail, WWW, webpage design, computer animation, spread sheet charts/figures, presentations with graphics and animations, etc. The course also covers business, accessibility, and relevant security issues in the use of computers and Internet.</td>
 *             </tr>	
 *          </tbody>
 *      </table>
 * 	    </div>
 *    </div>
 * </div>
 *  <h2>COMP 1001 - Exploring Multimedia and Internet Computing (3 units)</h2>
 *  <table class="sections" width="1012">
 *   <tbody>
 *    <tr>
 *        <th width="85">Section</th><th width="190" style="text-align: left">Date &amp; Time</th><th width="160" style="text-align: left">Room</th><th width="190" style="text-align: left">Instructor</th><th width="45">Quota</th><th width="45">Enrol</th><th width="45">Avail</th><th width="45">Wait</th><th width="81">Remarks</th>
 *    </tr>
 *    <tr class="newsect secteven">
 *        <td align="center">L1 (1765)</td>
 *        <td>We 02:00PM - 03:50PM</td><td>Rm 5620, Lift 31-32 (70)</td><td><a href="/wcq/cgi-bin/1830/instructor/LEUNG, Wai Ting">LEUNG, Wai Ting</a></td><td align="center">67</td><td align="center">0</td><td align="center">67</td><td align="center">0</td><td align="center">&nbsp;</td></tr><tr class="newsect sectodd">
 *        <td align="center">LA1 (1766)</td>
 *        <td>Tu 09:00AM - 10:50AM</td><td>Rm 4210, Lift 19 (67)</td><td><a href="/wcq/cgi-bin/1830/instructor/LEUNG, Wai Ting">LEUNG, Wai Ting</a></td><td align="center">67</td><td align="center">0</td><td align="center">67</td><td align="center">0</td><td align="center">&nbsp;</td>
 *    </tr>
 *   </tbody>
 *  </table>
 * </div>
 *}
 *</pre>
 * <br>
 * The code 
 * <pre>
 * {@code
 * List<?> items = (List<?>) page.getByXPath("//div[@class='course']");
 * }
 * </pre>
 * extracts all result-row and stores the corresponding HTML elements to a list called items. Later in the loop it extracts the anchor tag 
 * &lsaquo; a &rsaquo; to retrieve the display text (by .asText()) and the link (by .getHrefAttribute()).   
 * 
 *
 */
public class Scraper {
	private WebClient client;

	/**
	 * Default Constructor 
	 */
	public Scraper() {
		client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
	}

	/**
	 * Validate a section (L, LA, T)
	 * @param Code the section code
	 * @return return true if it is valid
	 */
	private boolean validateSection(String Code) {
		// validate the section
		String CodeType = Code.replaceAll("[0-9]", "");
		if (!CodeType.contains("L") && !CodeType.contains("LA") && !CodeType.contains("T"))
			return false;
		else
			return true;
	}
	
	/**
	 * Add a section to a course
	 * @param e HTML code
	 * @param c the course
	 * @return return true if it is a valid section
	 */
	private boolean addSection(HtmlElement e, Course c) {
		String secFullTitle = e.getChildNodes().get(1).asText();
		String ID = secFullTitle.substring(secFullTitle.indexOf('(') + 1, secFullTitle.indexOf(')') ); // get the ID, the number inside the (bracket)
		String Code = secFullTitle.substring(0, secFullTitle.indexOf('(') - 1); //get the code, i.e.: L1 / L2 / LA1 

		// create a new section in the course if it's valid
		if (validateSection(Code)) {
			String instructors = e.getChildNodes().get(5).asText();
			Section sec = new Section();
			
			sec.setID(Integer.parseInt(ID));
			sec.setCode(Code);
			sec.setInstructor(instructors);
			
			c.addSection(sec);
			return true;
		}
		return false;
	}
	
	/**
	 * Add a slot to a section
	 * @param e HTML code
	 * @param sec the section
	 * @param secondRow if it is second row of time slot, e.g.: Mo 0900-1030 & Fr 1200-1330 
	 */
	private void addSlot(HtmlElement e, Section sec, boolean secondRow) {
		String times[] =  e.getChildNodes().get(secondRow ? 0 : 3).asText().split("\\s+");
		String venue = e.getChildNodes().get(secondRow ? 1 : 4).asText();
		if (times[0].equals("TBA"))
			return;
		// if time contains the date, e.g.: "19-FEB-2020 - 19-MAY-2020\nTuTh 09:00AM - 10:20AM" instead of just "TuTh 09:00AM - 10:20AM"
		if (times[1].equals("-") ) {
			for (int j = 0; j < times[3].length(); j+=2) {
				String code = times[3].substring(j , j + 2);
				if (Slot.DAYS_MAP.get(code) == null)
					break;
				Slot s = new Slot();
				s.setDay(Slot.DAYS_MAP.get(code));
				s.setStart(times[4]);
				s.setEnd(times[6]);
				s.setVenue(venue);
				sec.addSlot(s);	
			}
		} else {
			for (int j = 0; j < times[0].length(); j+=2) {
				String code = times[0].substring(j , j + 2);
				if (Slot.DAYS_MAP.get(code) == null)
					break;
				Slot s = new Slot();
				s.setDay(Slot.DAYS_MAP.get(code));
				s.setStart(times[1]);
				s.setEnd(times[3]);
				s.setVenue(venue);
				sec.addSlot(s);	
			}
		}

	}
	
	private List<String> allSubCount(String baseurl, String term) {

		try {
			
			HtmlPage page = client.getPage(baseurl + "/" + term + "/");
			List<?> depts = (List<?>) page.getByXPath("//div[@class='depts']/a");
			Vector<String> result = new Vector<String>();
			
			for (int i =0; i < depts.size(); i++) {
				String name = new String();
				HtmlElement htmlItem = (HtmlElement) depts.get(i);
				name = htmlItem.getTextContent();
				result.add(name);
			}
			
			client.close();
			return result;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	private List<String> scrapeSqf(String baseurl){
		try {
			HtmlPage page = client.getPage(baseurl);

			List<?> table_list = (List<?>) page.getByXPath("//table");
			Vector<String> result = new Vector<String>();
			
			for(int i = 0; i < table_list.size(); i++) {
				HtmlElement table = (HtmlElement) table_list.get(i);
				//using try and catch is because some table not follow tr->th order
				try {
					HtmlElement header = (HtmlElement) table.getFirstByXPath(".//tbody/tr[1]/th[1]");
					
					//trim()is not working, therefore use replace(). Because the strings are different with space &nbsp
					if(header.getTextContent().replaceAll("\u00A0", "").equals("Course")) {
						List<?> row_list = (List<?>) table.getByXPath(".//tbody/tr");
						
						for(int j = 0; j < row_list.size(); j++) {
							try {
								HtmlElement row = (HtmlElement) row_list.get(j);
								HtmlElement temp = (HtmlElement)row.getFirstByXPath(".//td[1]");
								//add result without the last one, because last one is Department overall or course group overall
								if(!((HtmlElement) row.getFirstByXPath(".//td[1]")).getTextContent().replaceAll("\u00A0", "").equals("") && j+1<row_list.size()) {
									result.add(((HtmlElement) row.getFirstByXPath(".//td[2]")).getTextContent());		
								}
							}catch(Exception e) {
								//System.out.println(e);
							}
						}
					}
				}catch(Exception e) {
					System.out.println(e);
				}
			}
			client.close();
			return result;
			
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * This function scrape the content on web and put them in the the 
	 * structure of courses, sections and slots
	 * @param baseurl the domain name and the folder of the course catalog
	 * @param term the term to scrape, e.g.: 1910
	 * @param sub the subject to scrape, e.g.: COMP
	 * @return a list of courses
	 */
	public List<Course> scrape(String baseurl, String term, String sub) {

		try {
			
			HtmlPage page = client.getPage(baseurl + "/" + term + "/subject/" + sub);
			
			List<?> items = (List<?>) page.getByXPath("//div[@class='course']");
			
			Vector<Course> result = new Vector<Course>();

			// Loop all course in this list
			for (int i = 0; i < items.size(); i++) {
				Course c = new Course();
				HtmlElement htmlItem = (HtmlElement) items.get(i);
				
				HtmlElement title = (HtmlElement) htmlItem.getFirstByXPath(".//h2");
				c.setTitle(title.asText());
				
				List<?> popupdetailslist = (List<?>) htmlItem.getByXPath(".//div[@class='popupdetail']/table/tbody/tr");
				HtmlElement exclusion = null;
				for ( HtmlElement e : (List<HtmlElement>)popupdetailslist) {
					HtmlElement t = (HtmlElement) e.getFirstByXPath(".//th");
					HtmlElement d = (HtmlElement) e.getFirstByXPath(".//td");
					
					if (t.asText().equals("EXCLUSION")) {
						c.setExclusion(d.asText());
					}else if (t.asText().equals("ATTRIBUTES")) {
						if(d.asText().contains("Common Core") && d.asText().contains("for 4Y programs")) {
							c.setCC(true);
						}
					}else if (t.asText().equals("DESCRIPTION")) {
						c.setDescription(d.asText());
					}
				}
				
				List<?> sections = (List<?>) htmlItem.getByXPath(".//tr[contains(@class,'newsect')]");
				
				// Loop all sections in this course
				for ( HtmlElement e: (List<HtmlElement>)sections) {
					
					// if section is invalid, no need to add slot
					if(addSection(e, c)) {
						addSlot(e, c.getLastSection(), false);
						
						// check if there is second row, i.e.: Mo 1330-1500 && Fr 0900-1030, instead of TuTh 1330-1500
						e = (HtmlElement)e.getNextSibling();
						if (e != null && !e.getAttribute("class").contains("newsect")) {
							addSlot(e, c.getLastSection(), true);
						}
					}
				}
				
				// if this course has more than one section, add to the result
				if(c.getNumSections() != 0) {
					result.add(c);
				}
			}
			client.close();
			return result;
		} catch (Exception e) {
			System.out.println("404 page not found: make sure the URL, term and subject are valid. ");
			// System.out.println(e);
		}
		return null;
	}
	

}
