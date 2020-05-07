package comp3111.coursescraper;

import java.util.List;
import java.util.Vector;

public class Subject {
	private Vector<List<Course>> courses;
	private int numCourse;
	
	public Subject() {
		numCourse = 0;
	}
	public void addCourseList(List<Course> c) {
		courses.add(c);
		numCourse += c.size();
	}
	
}
