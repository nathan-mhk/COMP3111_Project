package comp3111.coursescraper;

import java.util.*;

/**
 * <b>Section of a course</b><br>
 * Section stores a section of a course, e.g.: L1, LA2, T3. <br>
 * Only Lecture (L), Lab (LA), or Tutorial (T) are valid. <br>
 * Each section has a unique four digit ID in that semester. <br>
 * Each section can have 0-3 time slots. <br>
 * 
 * @author ethan
 */

public class Section {
	private static final int DEFAULT_MAX_SLOT = 3;
	
	private int sectionID;
	private String sectionCode;
	private String sectionInstructor; // May be multiple instructors separated with '\n'
	private List<Slot> slots;
	private int numSlots;
	
	/**
	 * This is the contructor of the Section class. It initializes
	 * the array of slots as null and set numSlots to zero.
	 */
	public Section() {
		slots = Arrays.asList(new Slot[DEFAULT_MAX_SLOT]);
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++)
			slots.set(i, null);
		numSlots = 0;
	}
	
	/**
	 * This function duplicates a Section object
	 * @return This return the cloned Section
	 */
	@Override
	public Section clone() {
		Section s = new Section();
		s.sectionID = this.sectionID;
		s.sectionCode = this.sectionCode;
		s.sectionInstructor = this.sectionInstructor;
		for(int i = 0; i < numSlots; i++)
			s.addSlot(this.getSlot(i));
		return s;
	}

	/**
	 * This function return the four digit ID of this section
	 * @return This return the section's ID
	 */
	public int getID() {
		return sectionID;
	}
	
	/**
	 * This function setup the four digit ID of a section
	 * @param id This is the ID to be setup
	 */
	public void setID(int id){
		sectionID = id;
	}
	
	/**
	 * This function return the code of this section
	 * @return This return the section's code
	 */
	public String getCode() {
		return sectionCode;
	}

	/**
	 * This function setup the code of a section
	 * @param code This is the code to be setup
	 */
	public void setCode(String code) {
		sectionCode = code;
	}

	/**
	 * This function return the instructors of this section
	 * @return This return the section's instructors
	 */
	public String getInstructor() {
		return sectionInstructor;
	}
	
	/**
	 * This function setup the instructors of a section
	 * @param instructor This is the instructors to be setup
	 */
	public void setInstructor(String instructor) {
		sectionInstructor = instructor;
	}

	/**
	 * This function return a list of slots in this section
	 * @return list of slots
	 */
	public List<Slot> getSlots() {
		return slots;
	}

	/**
	 * This function return a specific slots in this section
	 * @return This return the slot request
	 * @param i This is the index of slot to be return
	 */
	public Slot getSlot(int i) {
		if (i >= 0 && i < numSlots)
			return slots.get(i);
		return null;
	}

	/**
	 * This function set the list of slots for this sections
	 * @param slots the slots to be set
	 */
	public void setSLots(List<Slot> slots) {
		this.slots = slots;
	}
	
	/**
	 * This function add a slot to the list of slots in this section
	 * @param s This is the slot to be added
	 */
	public void addSlot(Slot s) {
		if (numSlots >= DEFAULT_MAX_SLOT)
			return;
		slots.set(numSlots++, s.clone());
	}
	
	/**
	 * This function return the number of slots in this section
	 * @return This return the number of slots
	 */
	public int getNumSlots() {
		return numSlots;
	}
}
