package comp3111.coursescraper;

import java.util.*;

public class Course {
	private static final int DEFAULT_MAX_SLOT = 20;
	
	private String title; 
	private String description;
	private String exclusion;
	private List<Section> sections;
	private int numSections;
	
	public Course() {
		sections = Arrays.asList(new Section[DEFAULT_MAX_SLOT]);
		for (int i = 0; i < DEFAULT_MAX_SLOT; i++)
			sections.set(i, null);
		numSections = 0;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the exclusion
	 */
	public String getExclusion() {
		return exclusion;
	}

	/**
	 * @param exclusion the exclusion to set
	 */
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}
	
	public List<Section> getSections() {
		return sections;
	}

	public Section getSection(int i) {
		if (i >= 0 && i < numSections)
			return sections.get(i);
		return null;
	}

	public Section getLastSection() {
		return sections.get(numSections - 1);
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public void addSection(Section s) {
		if (numSections >= DEFAULT_MAX_SLOT)
			return;
		sections.set(numSections++, s.clone());
	}

	public int getNumSections() {
		return numSections;
	}
	
	public void setNumSections(int numSections) {
		this.numSections = numSections;
	}
	

}
