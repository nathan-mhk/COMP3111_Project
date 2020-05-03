package comp3111.coursescraper;

import java.util.*;

/**
 * <b>A Course offered in the semester</b><br>
 * Course stores a course, e.g.: COMP 3111 - Software Engineering. <br>
 * A course must has a valid section. <br>
 * Each course can technically has 1 to unlimited sections. <br>
 * 
 */
public class Course {
	private static final int DEFAULT_MAX_SLOT = 50;
	
	private String title; 
	private String description;
	private String exclusion;
	private List<Section> sections;
	private int numSections;
	private boolean isCommonCore;
	
	/**
	 * This function is the constructor of Course
	 * It initialize the array of sections and set the number 
	 * of sections to zero 
	 */
	public Course() {
		sections = Arrays.asList(new Section[DEFAULT_MAX_SLOT]);
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++)
			sections.set(i, null);
		numSections = 0;
		isCommonCore = false;
	}

	/**
	 * This function duplicates a Course object
	 * @return This return the cloned Course
	 */
	@Override
	public Course clone() {
		Course c = new Course();
		c.title = this.title;
		c.description = this.description;
		c.exclusion = this.exclusion;
		c.isCommonCore = this.isCommonCore;

		for (int i = 0; i < numSections; i++) {
			c.addSection(this.getSection(i));
		}
		return c;
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
	

	/**
	 * This function return a list of sections in this course
	 * @return list of sections
	 */
	public List<Section> getSections() {
		return sections;
	}

	/**
	 * This function return a specific section at a specific index
	 * @param i The index of the section 
	 * @return This is the section returned
	 */
	public Section getSection(int i) {
		if (i >= 0 && i < numSections)
			return sections.get(i);
		return null;
	}

	/**
	 * This function return the last section in this course
	 * @return The last section located in the array of sections in this course
	 */
	public Section getLastSection() {
		return sections.get(numSections - 1);
	}

	/**
	 * This function set the list of sections of this course
	 * @param sections The list of sections to be set
	 */
	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	/**
	 * This function add a section to the array of sections
	 * @param s The section to be added
	 */
	public void addSection(Section s) {
		if (numSections >= DEFAULT_MAX_SLOT)
			return;
		sections.set(numSections++, s.clone());
	}

	/**
	 * This function return the number of sections in this course
	 * @return This is the number of sections in this course
	 */
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
