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

    private static Controller controller = null;
    
    /**
     * Set the controller of ListEntry
     * 
     * @param c The controller that the data model belongs to
     */
    public static void setController(Controller c) {
        controller = c;
    }

    /**
     * Construct an entry according to the given couse and section
     * @param course The course that the entry belongs to
     * @param section The section that the entry belongs to
     */
    public ListEntry(Course course, Section section) {
        final int index = 0;

        String[] temp = course.getTitle().split("-");
        this.courseCode = new SimpleStringProperty(temp[index].trim());

        temp = temp[index + 1].split("\\(");
        this.courseName = new SimpleStringProperty(temp[index].trim());

        this.lectureSection = new SimpleStringProperty(section.getCode());
        this.instructor = new SimpleStringProperty(section.getInstructor());
        this.enrolled = new SimpleBooleanProperty(section.isEnrolled());

        this.course = course;
        this.section = section;

        enrolled.addListener((observable, oldValue, newValue) -> {
            this.section.setEnrollStatus(newValue);
            controller.updateEnrolledCourses(this.course, this.section);
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
