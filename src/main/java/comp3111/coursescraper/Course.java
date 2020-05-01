package comp3111.coursescraper;

import java.util.*;

<<<<<<< HEAD
/**
 * <b>A Course offered in the semester</b><br>
 * Course stores a course, e.g.: COMP 3111 - Software Engineering. <br>
 * A course must has a valid section. <br>
 * Each course can technically has 1 to unlimited sections. <br>
 * 
 */
=======
>>>>>>> nathan_dev
public class Course {
	private static final int DEFAULT_MAX_SLOT = 50;
	
	private String title; 
	private String description;
	private String exclusion;
	private List<Section> sections;
	private int numSections;
	private boolean isCommonCore;
	
<<<<<<< HEAD
	/**
	 * This function is the constructor of Course
	 * It initialize the array of sections and set the number 
	 * of sections to zero 
	 */
=======
>>>>>>> nathan_dev
	public Course() {
		sections = Arrays.asList(new Section[DEFAULT_MAX_SLOT]);
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++)
			sections.set(i, null);
		numSections = 0;
<<<<<<< HEAD
		isCommonCore = false;
=======
>>>>>>> nathan_dev
	}

	/**
	 * This function return the course title
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This function setup the course title
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * This function return the course description
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * This function setup the course description
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * This function return the course exclusion
	 * @return the exclusion
	 */
	public String getExclusion() {
		return exclusion;
	}

	/**
	 * This function setup the course exclusion
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}
	
<<<<<<< HEAD
	/**
	 * This function return a list of sections in this course
	 * @return list of sections
	 */
=======
>>>>>>> nathan_dev
	public List<Section> getSections() {
		return sections;
	}

<<<<<<< HEAD
	/**
	 * This function return a specific section at a specific index
	 * @param i The index of the section 
	 * @return This is the section returned
	 */
=======
>>>>>>> nathan_dev
	public Section getSection(int i) {
		if (i >= 0 && i < numSections)
			return sections.get(i);
		return null;
	}

<<<<<<< HEAD
	/**
	 * This function return the last section in this course
	 * @return The last section located in the array of sections in this course
	 */
=======
>>>>>>> nathan_dev
	public Section getLastSection() {
		return sections.get(numSections - 1);
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}
<<<<<<< HEAD
	
	/**
	 * This function add a section to the array of sections
	 * @param s The section to be added
	 */
=======

>>>>>>> nathan_dev
	public void addSection(Section s) {
		if (numSections >= DEFAULT_MAX_SLOT)
			return;
		sections.set(numSections++, s.clone());
	}

	public int getNumSections() {
		return numSections;
	}
	
	/**
	 * This function setup the number of sections in this course
	 * @param numSections This is the number of sections to be set
	 */
	public void setNumSections(int numSections) {
		this.numSections = numSections;
	}
	
	/**
	 * This function return if this course is common core
	 * @return This show whether this course is common core
	 */
	public boolean getCC() {
		return isCommonCore;
	}
	
	/**
	 * This function setup if this course is common core
	 * @param state This is the state to be set
	 */
	public void setCC(boolean state) {
		this.isCommonCore = state;
	}
}
