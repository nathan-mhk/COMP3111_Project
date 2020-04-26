package comp3111.coursescraper;


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
		
		sec.setID(3111);
		
		for (int k = 0; k < 25; k++)
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
	public void testGetNumSlots() {
		assertEquals(i.getNumSections(), 20);
	}
	
	@Test
	public void testGetSlot() {
		assertEquals(i.getSection(5).getID(), 3111);
		assertEquals(i.getSection(25), null);
	}
}
