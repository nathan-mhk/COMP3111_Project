package comp3111.coursescraper;

import java.util.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

    private BooleanProperty enrolled;

    // The course that this entry belongs to
    private Course course;
    // The section that this entry belongs to
    private Section section;

    public ListEntry(Course course, Section section) {
        final int index = 0;
        final int courseNameIndex = index + 1;

        String[] temp = course.getTitle().split("-");
        this.courseCode = new SimpleStringProperty(temp[index].trim());

        temp = temp[courseNameIndex].split("\\(");
        this.courseName = new SimpleStringProperty(temp[index].trim());

        this.lectureSection = new SimpleStringProperty(section.getCode());
        this.instructor = new SimpleStringProperty(section.getInstructor());
        this.enrolled = new SimpleBooleanProperty(section.isEnrolled());

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

    public boolean getEnrolled() {
        return enrolled.get();
    }

    public void setEnrolled(boolean enrolled) {
        this.enrolled.set(enrolled);
        section.setEnrollStatus(enrolled);
    }
}
