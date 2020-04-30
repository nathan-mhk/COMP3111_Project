package comp3111.coursescraper;

public class Section {
	private static final int DEFAULT_MAX_SLOT = 3;
	
	private int sectionID;
	private String sectionCode;
	private String sectionInstructor; // May be multiple instructors separated with '\n'
	private Slot [] slots;
	private int numSlots;
	
	public Section() {
		slots = new Slot[DEFAULT_MAX_SLOT];
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++) slots[i] = null;
		numSlots = 0;
	}
	
	@Override
	public Section clone() {
		Section s = new Section();
		s.sectionID = this.sectionID;
		s.sectionCode = this.sectionCode;
		s.sectionInstructor = this.sectionInstructor;
		for(int i = 0; i < numSlots; i++) {
			s.addSlot(this.getSlot(i));
		}
		return s;
	}
	
	public void setID(int id){
		sectionID = id;
	}
	
	public int getID() {
		return sectionID;
	}
	
	public void setCode(String code) {
		sectionCode = code;
	}
	
	public String getCode() {
		return sectionCode;
	}
	
	public void setInstructor(String instructor) {
		sectionInstructor = instructor;
	}
	
	public void addSlot(Slot s) {
		if (numSlots >= DEFAULT_MAX_SLOT)
			return;
		slots[numSlots++] = s.clone();
	}
	
	public Slot getSlot(int i) {
		if (i >= 0 && i < numSlots)
			return slots[i];
		return null;
	}
	
	public int getNumSlots() {
		return numSlots;
	}
}
