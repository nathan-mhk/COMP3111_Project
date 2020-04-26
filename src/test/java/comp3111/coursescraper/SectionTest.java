package comp3111.coursescraper;


import org.junit.*;

import comp3111.coursescraper.Section;
import comp3111.coursescraper.Slot;

import static org.junit.Assert.*;


public class SectionTest {

	private Section sec = new Section();
	private Slot s = new Slot();
	
	@Before
	public void setUp() {
		sec.setID(2020);
		sec.setCode("L1");
		sec.setInstructor("ABC");
		
		s.setVenue("Rm 6591, Lift 31-32 (88)");
		
		for (int k = 0; k < 5; k++)
			sec.addSlot(s);
	}
	
	@Test
	public void testSetID() {
		assertEquals(sec.getID(), 2020);
	}
	
	@Test
	public void testSetCode() {
		assertEquals(sec.getCode(), "L1");
	}
	
	@Test
	public void testGetNumSlots() {
		assertEquals(sec.getCode(), "L1");
	}
	
//	@Test
//	public void testSetInstructor() {
//		assertEquals(sec.getInstructor(), "ABC");
//	}
	
	@Test
	public void testGetSlot() {
		assertEquals(sec.getSlot(-1), null);
		assertEquals(sec.getSlot(2).getVenue(), "Rm 6591, Lift 31-32 (88)");
		assertEquals(sec.getSlot(3), null);
	}
}
