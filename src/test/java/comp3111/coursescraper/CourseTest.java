package comp3111.coursescraper;

import java.util.*;
import org.junit.*;

import comp3111.coursescraper.Course;
import comp3111.coursescraper.Section;

import static org.junit.Assert.*;


public class CourseTest {

	private Course i = new Course();
	private Section sec = new Section();
	
	@Before
	public void setUp() {
		i.setTitle("COMP 3111 - Software Engineering");
		i.setDescription("Methods and tools for planning, designing, implementing, validating, and maintaining large software systems. Project work to build a software system as a team, using appropriate software engineering tools and techniques.");
		i.setExclusion("COMP 3111H, ISOM 3210, RMBI 4420 (prior to 2016-17)");
		i.setNumSections(0);
		i.setCC(true);
		
		sec.setID(3111);
		
		for (int k = 0; k < 10; k++)
			i.addSection(sec);
		
		sec.setID(2011);
		
		for (int k = 10; k < 65; k++)
			i.addSection(sec);
	}

	@Test
	public void testSetTitle() {
		assertEquals(i.getTitle(), "COMP 3111 - Software Engineering");
	}

	@Test
	public void testSetDescription() {
		assertEquals(i.getDescription(), "Methods and tools for planning, designing, implementing, validating, and maintaining large software systems. Project work to build a software system as a team, using appropriate software engineering tools and techniques.");
	}

	@Test
	public void testSetExclusion() {
		assertEquals(i.getExclusion(), "COMP 3111H, ISOM 3210, RMBI 4420 (prior to 2016-17)");
	}

	@Test
	public void testGetNumSections() {
		assertEquals(i.getNumSections(), 50);
	}
	
	@Test
	public void testGetSection() {
		assertEquals(i.getSection(5).getID(), 3111);
		assertEquals(i.getLastSection().getID(), 2011);
		assertEquals(i.getSection(-1), null);
		assertEquals(i.getSection(50), null);
	}
	
	@Test
	public void testEquals() {
		Course c = null;
		assertFalse(i.equals(c));
		
		assertFalse(i.equals(sec));

		c = new Course();
		assertFalse(i.equals(c));

		c.setTitle("COMP 3111 - Software Engineering");
		assertFalse(i.equals(c));

		c.setDescription("Methods and tools for planning, designing, implementing, validating, and maintaining large software systems. Project work to build a software system as a team, using appropriate software engineering tools and techniques.");
		assertFalse(i.equals(c));

		c.setExclusion("COMP 3111H, ISOM 3210, RMBI 4420 (prior to 2016-17)");
		assertFalse(i.equals(c));

		c.setCC(true);
		assertTrue(i.equals(c));
	}
}
