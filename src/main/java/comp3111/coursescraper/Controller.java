package comp3111.coursescraper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;

import java.util.*;
import java.time.LocalTime;

public class Controller {

    @FXML
    private Tab tabMain;

    @FXML
    private TextField textfieldTerm;

    @FXML
    private TextField textfieldSubject;

    @FXML
    private Button buttonSearch;

    @FXML
    private TextField textfieldURL;

    @FXML
    private Tab tabStatistic;

    @FXML
    private ComboBox<?> comboboxTimeSlot;

    @FXML
    private Tab tabFilter;

    @FXML
    private Tab tabList;

    @FXML
    private Tab tabTimetable;

    @FXML
    private Tab tabAllSubject;

    @FXML
    private ProgressBar progressbar;

    @FXML
    private TextField textfieldSfqUrl;

    @FXML
    private Button buttonSfqEnrollCourse;

    @FXML
    private Button buttonInstructorSfq;

    @FXML
	private TextArea textAreaConsole;
	
	@FXML
	private AnchorPane anchorPaneFilter;
    
    private Scraper scraper = new Scraper();
    
    @FXML
    void allSubjectSearch() {
    	
    }

    @FXML
    void findInstructorSfq() {
    	buttonInstructorSfq.setDisable(true);
    }

    @FXML
    void findSfqEnrollCourse() {

    }
    
    private boolean isMainURLValid() {
    	// 4 digits only
    	if (textfieldTerm.getText().length() != 4 || !textfieldTerm.getText().matches("^[0-9]*$") ) {
    		textAreaConsole.setText("404 page not found: invalid term. ");
    		return false;
    	}
    	
    	// 4 UPPERCASE character only
    	if (textfieldSubject.getText().length() != 4 || !textfieldSubject.getText().matches("^[A-Z]*$") ) {
    		textAreaConsole.setText("404 page not found: invalid subject. ");
    		return false;
    	}
    	
    	// 404 page not found
    	List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    	if (v == null) {
    		textAreaConsole.setText("404 page not found: make sure the URL, term and subject are valid. ");
    		return false;
    	}
    	return true;
    }
    
    private void addStringFromArrayToList(List<String> list, String [] arr) {
    	for (int j = 0; j < arr.length; j++) {
			if (!list.contains(arr[j]) && !arr[j].contains("TBA")) {
				list.add(arr[j]);
			}
		}
	}
	
	private List<Course> getListOfCourse() {
		return scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(), textfieldSubject.getText());
	}
    
    private String generateConsoleOutput(List<Course> v) {
    	String catalogOutput = "";
    	int courseCount = 0;
    	int sectionCount = 0;
    	LocalTime TIME_THREE_TEN = LocalTime.of(15,10);
    	List<String> allInstructors = new ArrayList<String>();
    	List<String> threeTenInstructors = new ArrayList<String>();
    	
    	// loop all courses in this list
    	for (Course c : v) {
    		String newline = c.getTitle() + "\n";
    		courseCount += 1;
    		
    		// loop all sections in this course
    		for (int i = 0; i < c.getNumSections(); i++) {
    			Section sec = c.getSection(i);
    			newline += "\tSection: " + sec.getCode() + " (" + sec.getID() + ")\n";
    			
    			sectionCount += 1;
    			boolean timeflag = false;
    			
    			// add all new instructors to the list
    			String [] secInstructor = sec.getInstructor().split("\n");
    			addStringFromArrayToList(allInstructors, secInstructor);
    			
    			// loop all slots in this section
    			for (int j = 0; j < sec.getNumSlots(); ) {
        			Slot t = sec.getSlot(j);
        			j += 1;
        			newline += "\t\tSlot " + j + ": " + t + "\n";
        			
        			// flag true if instructors have class at three ten
        			if(t.getDay() == 1 && t.getStart().isBefore(TIME_THREE_TEN) && t.getEnd().isAfter(TIME_THREE_TEN)) {
        				timeflag = true;
        			}
        		}
    			
    			// add instructors who have class at three ten to the list
    			if (timeflag) {
    				addStringFromArrayToList(threeTenInstructors, secInstructor);
    			}
    		}
    		// append the result of this course
    		catalogOutput += "\n" + newline;
    	}
    	
    	// get and sort list of instructors that are not teaching at Tu1510 
    	allInstructors.removeAll(threeTenInstructors);
    	allInstructors.sort(Comparator.naturalOrder());
    	String noThreeTenInstructors = "";
    	for (String i : allInstructors) {
    		noThreeTenInstructors += i + "\n";
    	}
    	
    	// combine the outputs and return
    	String sectionCountOutput = "Total Number of Different sections in this search: " + sectionCount + "\n";
    	String courseCountOutput = "Total Number of Courses in this search: " + courseCount + "\n";
    	String instructorListOutput = "Instructors who has teaching assignment this term but does not need to teach at Tu 3:10pm: \n" + noThreeTenInstructors + "\n";
    	String consoleOutputResult = courseCountOutput + sectionCountOutput + instructorListOutput + catalogOutput;
    	
    	return consoleOutputResult; 
    }

	private void clearConsoleOutput() {
		textAreaConsole.setText("");
	}

	/**
	 * Generate texts and set it to textAreaConsole
	 * 
	 * @param courses a list of courses
	 */
	private void setConsoleOutput(List<Course> courses) {
		// reset console text
		clearConsoleOutput();
		textAreaConsole.setText(generateConsoleOutput(courses));
	}

	/**
	 * @return true if any filter is applied, else false
	 */
	private boolean containFilters() {
		for (Node node : anchorPaneFilter.getChildren()) {
			if (node instanceof CheckBox) {
				if (((CheckBox) node).isSelected()) {
					return true;
				}
			}
		}
		return false;
	}

	private void fetch() {
		// scrape using scraper and set the console output
		if (containFilters()) {
			setConsoleOutput(Filter.filterCourses(getListOfCourse()));
		} else {
			setConsoleOutput(getListOfCourse());
		}
	}

    @FXML
    void search() {
    	// check if the URL is valid
    	if(!isMainURLValid()) {
    		return;
    	}
    	fetch();
	}
	
	/**
	 * Handling UI and setting fiteres only
	 */
	@FXML
	void checkFilters(ActionEvent event) {
		if (event.getSource() instanceof Button) {
			Button btn = (Button) event.getSource();

			boolean allChecked = Filter.toggleAll();
			String btnString;

			if (allChecked) {
				btnString = "De-select All";
			} else {
				btnString = "Select All";
			}
			btn.setText(btnString);

			// Check/Uncheck all boxes visually
			for (Node node : anchorPaneFilter.getChildren()) {
				if (node instanceof CheckBox) {
					((CheckBox) node).setSelected(allChecked);
				}
			}

		} else {
			CheckBox checkBox = (CheckBox) event.getSource();
			Filter.check(checkBox.getText());
		}

		// Update the search result
		fetch();
	}
}
