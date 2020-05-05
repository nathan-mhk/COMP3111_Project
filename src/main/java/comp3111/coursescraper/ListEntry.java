package comp3111.coursescraper;

import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Data model class for entries inside List tab
 * 
 * @author Nathan
 */

// Must be public, otherwise will get IllegalAccessException
public class ListEntry {

    private StringProperty courseCode;
    private StringProperty lectureSection;
    private StringProperty courseName;
    private StringProperty instructor;

    private BooleanProperty enrolled;

    private Course course;
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

    /**
     * Get the course code of the this entry
     * 
     * @return The course code
     */
    public String getCourseCode() {
        return courseCode.get();
    }

    /**
     * Get the lecture section of this entry
     * 
     * @return The lecture section
     */
    public String getLectureSection() {
        return lectureSection.get();
    }

    /**
     * Get the name of the course of this entry
     * 
     * @return The name of the course
     */
    public String getCourseName() {
        return courseName.get();
    }

    /**
     * Get the instructors of this entry
     * 
     * @return The instructors
     */
    public String getInstructor() {
        return instructor.get();
    }

    /**
     * Get the course of this entry
     * 
     * @return The course that this entry belongs to
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Get the section of this entry
     * 
     * @return The section that this entry belongs to
     */
    public Section getSection() {
        return section;
    }

    /**
     * Get the enrollment status of this entry
     * 
     * @return True if this entry is enrolled (checkbox is selected)
     */
    public boolean getEnrolled() {
        return enrolled.get();
    }

    /**
     * Get the BooleanProperty of the checkbox of this entry
     * 
     * @return The BooleanProperty of this entry
     */
    public BooleanProperty enrolledProperty() {
        return enrolled;
    }
}
