package comp3111.coursescraper;

import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * An entry to the tableview inside List tab
 * Must be public, otherwise will get IllegalAccessException
 * 
 * @author Nathan
 */
public class ListEntry {

    // course title: COMP 3111 - Software Engineering (4 units)

    private StringProperty courseCode;
    private StringProperty lectureSection;
    private StringProperty courseName;
    private StringProperty instructor;

    private boolean enrolled;

    // The course that this entry belongs to
    private Course course;
    // The section that this entry belongs to
    private Section section;

    public ListEntry(Course course, Section section) {
        final int index = 0;
        final int courseNameIndex = index + 1;

        String[] temp = course.getTitle().split("-");
        courseCode = new SimpleStringProperty(temp[index].trim());

        temp = temp[courseNameIndex].split("\\(");
        courseName = new SimpleStringProperty(temp[index].trim());

        lectureSection = new SimpleStringProperty(section.getCode());
        instructor = new SimpleStringProperty(section.getInstructor().replaceAll("\\n",", "));
        enrolled = section.isEnrolled();

        this.course = course;
        this.section = section;
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public String getLectureSection() {
        return lectureSection.get();
    }

    public String getCourseName() {
        return courseName.get();
    }

    public String getInstructor() {
        return instructor.get();
    }

    public Course getCorrespondingCourse() {
        return course;
    }

    public Section getCorrespondingCSection() {
        return section;
    }

    public boolean isEnrolled() {
        return enrolled;
    }

    public void enrollSection() {
        enrolled = true;
        section.setEnrollStatus(enrolled);
    }
}
