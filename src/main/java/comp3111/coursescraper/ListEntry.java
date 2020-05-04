package comp3111.coursescraper;

import java.util.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Data model class for tableview entries inside List tab
 * Must be public, otherwise will get IllegalAccessException
 * 
 * @author Nathan
 */
public class ListEntry {

    private StringProperty courseCode;
    private StringProperty lectureSection;
    private StringProperty courseName;
    private StringProperty instructor;

    private BooleanProperty enrolled;

    // The course that this entry belongs to
    private Course course;
    // The section that this entry belongs to
    private Section section;

    // Delegate
    private static Controller ctrl = null;


    public ListEntry(Course course, Section section, Controller controller) {
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

        if (ctrl == null) {
            ctrl = controller;
        }

        enrolled.addListener((observable, oldValue, newValue) -> {
            this.section.setEnrollStatus(newValue);
            ctrl.updateEnrolledCourses(course, section, newValue);
        });
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

    public Section getCorrespondingSection() {
        return section;
    }

    public boolean getEnrolled() {
        return enrolled.get();
    }

    public BooleanProperty enrolledProperty() {
        return enrolled;
    }

    public void debugMsg() {
        System.out.println(course.getTitle() + "\n" + section.getCode() + " : " + enrolled.getValue());
    }
}
