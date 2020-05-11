package comp3111.coursescraper;

import java.util.*;
import org.junit.*;

import comp3111.coursescraper.Section;
import comp3111.coursescraper.Slot;

import static org.junit.Assert.*;


public class SectionTest {

	private Section sec = new Section();
	private Slot s = new Slot();

	private List<Slot> slots = new Vector<Slot>();
	
	@Before
	public void setUp() {
		sec.setID(2020);
		sec.setCode("L1");
		sec.setInstructor("ABC");
		
		s.setVenue("Rm 6591, Lift 31-32 (88)");
		
		for (int k = 0; k < 5; k++)
			sec.addSlot(s);
			slots.add(s.clone());
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
		assertEquals(sec.getNumSlots(), 3);
	}
	
	@Test
	public void testSetInstructor() {
		assertEquals(sec.getInstructor(), "ABC");
	}
	
	@Test
	public void testClone() {
		Section newSec = sec.clone();
		assertEquals(newSec.getInstructor(), "ABC");
		assertEquals(newSec.getSlot(1).getVenue(), "Rm 6591, Lift 31-32 (88)");
	}
	
	@Test
	public void testGetSlot() {
		assertEquals(sec.getSlot(-1), null);
		assertEquals(sec.getSlot(2).getVenue(), "Rm 6591, Lift 31-32 (88)");
		assertEquals(sec.getSlot(3), null);
	}

	@Test
	public void testSlotsManipulating() {

		Slot slot = slots.get(0);
		slots.add(slot);

		// setSlots()
		sec.setSlots(slots);

		// setNumSlots()
		sec.setNumSlots(slots.size());
		assertEquals(slots.size(), sec.getNumSlots());
		
		// getSlots()
		assertEquals(slots, sec.getSlots());
		
		// setEnrollStatus()
		sec.setEnrollStatus(true);
		assertTrue(sec.isEnrolled());
		sec.setEnrollStatus(false);
		assertFalse(sec.isEnrolled());
	}

	@Test
	public void testEquals() {
		Section section = null;
		assertFalse(sec.equals(section));

		assertFalse(sec.equals(s));

		section = new Section();
		assertFalse(sec.equals(section));

		section.setID(2020);
		assertFalse(sec.equals(section));

		section.setCode("L1");
		assertFalse(sec.equals(section));

		section.setInstructor("ABC");
		assertTrue(sec.equals(section));		
	}
}
