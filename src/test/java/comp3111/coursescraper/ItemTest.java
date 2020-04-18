package comp3111.coursescraper;


import org.junit.*;

import comp3111.coursescraper.Course;
import comp3111.coursescraper.Slot;

import static org.junit.Assert.*;


public class ItemTest {

	private Course i = new Course();
	private Slot s = new Slot();
	
	@Before
	public void setUp() {
		i.setTitle("COMP 3111 - Software Engineering");
		i.setDescription("Methods and tools for planning, designing, implementing, validating, and maintaining large software systems. Project work to build a software system as a team, using appropriate software engineering tools and techniques.");
		i.setExclusion("COMP 3111H, ISOM 3210, RMBI 4420 (prior to 2016-17)");
		i.setNumSlots(0);
		
		s.setVenue("Rm 6591, Lift 31-32 (88)");
		s.setDay(4);
		
		for (int k = 0; k < 25; k++)
			i.addSlot(s);
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
		assertEquals(i.getNumSlots(), 20);
	}
	
	@Test
	public void testSetVenue() {
		assertEquals(s.getVenue(), "Rm 6591, Lift 31-32 (88)");
	}
	
	@Test
	public void testSetDay() {
		assertEquals(s.getDay(), 4);
	}
	
	@Test
	public void testGetSlot() {
		assertEquals(i.getSlot(5).getVenue(), "Rm 6591, Lift 31-32 (88)");
	}
	
	@Test
	public void testGetSlot2() {
		assertEquals(i.getSlot(25), null);
	}
}
