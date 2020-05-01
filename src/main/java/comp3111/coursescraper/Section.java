package comp3111.coursescraper;

import java.util.*;

public class Section {
	private static final int DEFAULT_MAX_SLOT = 3;
	
	private int sectionID;
	private String sectionCode;
	private String sectionInstructor; // May be multiple instructors separated with '\n'
	private List<Slot> slots;
	private int numSlots;
	
	public Section() {
		slots = Arrays.asList(new Slot[DEFAULT_MAX_SLOT]);
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++)
			slots.set(i, null);
		numSlots = 0;
	}
	
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

	public int getID() {
		return sectionID;
	}
	
	public void setID(int id){
		sectionID = id;
	}
	
	public String getCode() {
		return sectionCode;
	}

	public void setCode(String code) {
		sectionCode = code;
	}
	
	public String getInstructor() {
		return sectionInstructor;
	}

	public void setInstructor(String instructor) {
		sectionInstructor = instructor;
	}

	public List<Slot> getSlots() {
		return slots;
	}

	public Slot getSlot(int i) {
		if (i >= 0 && i < numSlots)
			return slots.get(i);
		return null;
	}

	public void setSLots(List<Slot> slots) {
		this.slots = slots;
	}
	
	public void addSlot(Slot s) {
		if (numSlots >= DEFAULT_MAX_SLOT)
			return;
		slots.set(numSlots++, s.clone());
	}
	
	public int getNumSlots() {
		return numSlots;
	}
}
