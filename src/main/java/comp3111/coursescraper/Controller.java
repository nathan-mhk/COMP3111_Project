package comp3111.coursescraper;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.util.Callback;

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
	
	@FXML
	private TableView<ListEntry> tableViewList;
	
	@FXML
	private TableColumn<ListEntry, String> courseCodeCol;
	
	@FXML
	private TableColumn<ListEntry, String> sectionCol;
	
	@FXML
	private TableColumn<ListEntry, String> courseNameCol;
	
	@FXML
	private TableColumn<ListEntry, String> instructorCol;
	
	@FXML
	private TableColumn<ListEntry, Boolean> enrollCol;
    
	private Scraper scraper = new Scraper();

	private boolean isFiltering = false;
	private boolean init = true;
	private String currentTerm = "";

	private List<Course> unfilteredCourses = Collections.emptyList();
	private List<Course> filteredCourses = Collections.emptyList();
	private Vector<Course> enrolledCourses = new Vector<Course>();

	private ObservableList<ListEntry> listEntries = FXCollections.observableArrayList();
    
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
		if (unfilteredCourses.isEmpty()) {
			unfilteredCourses = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(), textfieldSubject.getText());
		}
		return unfilteredCourses;
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
		String consoleOutputResult = "";

		if (!isFiltering) {
			String sectionCountOutput = "Total Number of Different sections in this search: " + sectionCount + "\n";
    		String courseCountOutput = "Total Number of Courses in this search: " + courseCount + "\n";
    		String instructorListOutput = "Instructors who has teaching assignment this term but does not need to teach at Tu 3:10pm: \n" + noThreeTenInstructors + "\n";
    		consoleOutputResult = courseCountOutput + sectionCountOutput + instructorListOutput + catalogOutput;
		} else {
			String filterResultCountOutput = "Number of courses from search that matches the filters: " + courseCount + "\n";
			consoleOutputResult = filterResultCountOutput + catalogOutput;
		}

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
	 * UI initialization
	 * Set up the TableColumns inside tableViewList
	 */
	private void setUpTableView() {
		tableViewList.setPlaceholder(new Label("No courses to display"));

		courseCodeCol.setCellValueFactory(new PropertyValueFactory<ListEntry, String>("courseCode"));
		sectionCol.setCellValueFactory(new PropertyValueFactory<ListEntry, String>("lectureSection"));
		courseNameCol.setCellValueFactory(new PropertyValueFactory<ListEntry, String>("courseName"));
		instructorCol.setCellValueFactory(new PropertyValueFactory<ListEntry, String>("instructor"));

		enrollCol.setCellValueFactory(new Callback<CellDataFeatures<ListEntry, Boolean>, ObservableValue<Boolean>>() {
			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<ListEntry, Boolean> param) {
				return param.getValue().enrolledProperty();
			}
		});

		enrollCol.setCellFactory(CheckBoxTableCell.forTableColumn(enrollCol));

		tableViewList.setItems(listEntries);
	}

	// FIXME: When dropping section, don't remove course!!!!!
	void updateEnrolledCourses(Course c, Section s, boolean enrolled) {
		Vector<Section> enrolledSections = new Vector<Section>();

		// Get a list of currently enrolled section for that course
		for (int i = 0; i < c.getNumSections(); ++i) {
			Section section = c.getSection(i);
			if (section.isEnrolled()) {
				enrolledSections.add(section.clone());
			}
		}
		
		if (enrolled) {
			enrolledSections.add(s);
		}
		if (enrolled || !enrolledSections.isEmpty()) {
			Course course = c.clone();
			course.setSections(enrolledSections);
			course.setNumSections(enrolledSections.size());

			/**
			 * Two courses are considered equals even if their sections are not equal
			 * Update the existing course inside enrolledCourses
			 */
			enrolledCourses.remove(course);
			enrolledCourses.add(course);

		} else {
			enrolledCourses.remove(c);
		}

		System.out.println("Enrolled course: " + enrolledCourses.size());
	}

	/**
	 * Get a list of unenrolled courses from filteredCourses
	 * 
	 * @return a list of unenrolled courses
	 */

	// Get a list of course that have all sections not enrolled
	// FIXME: Removed enrolled section!!!!!
	private List<Course> getNotEnrolledCourses() {
		Vector<Course> notEnrolled = new Vector<Course>();

		for (Course course : filteredCourses) {
			
			Vector<Section> notEnrolledSections = new Vector<Section>();

			for (int i = 0; i < course.getNumSections(); ++i) {
				Section section = course.getSection(i);

				if (!section.isEnrolled()) {
					notEnrolledSections.add(section.clone());
				}
			}
			// If the course contains unenrolled sections
			if (!notEnrolledSections.isEmpty()) {
				Course targetCourse = course.clone();
				targetCourse.setSections(notEnrolledSections);
				targetCourse.setNumSections(notEnrolledSections.size());

				notEnrolled.add(targetCourse);
			}
		}

		return notEnrolled;
	}

	// Create a list of listEntries, set it to displaying courses
	private void setListEntries(List<Course> courses) {
		for (Course course : courses) {
			for (int i = 0; i < course.getNumSections(); ++i) {
				Section section = course.getSection(i);

				listEntries.add(new ListEntry(course.clone(), section.clone(), this));
			}
		}
	}

	private void displayList() {
		if (init) {
			setUpTableView();
			init = false;
		}
		// Reset
		listEntries.clear();

		setListEntries(enrolledCourses);
		setListEntries(getNotEnrolledCourses());

		/**
		 * TODO:
		 * Visually check all checkboxes that is enrolled
		 * Get all CheckBoxTableCell inside a tableColumn?
		 */
	}

	private void fetch() {
		// updateEnrolledCourses();
		// scrape using scraper and set the console output
		if (!isFiltering) {
			setConsoleOutput(getListOfCourse());
		} else {
			filteredCourses = Filter.filterCourses(getListOfCourse());
			setConsoleOutput(filteredCourses);
			displayList();
		}
	}

    @FXML
    void search() {
		isFiltering = false;

		// Reset the unfiltered course
		unfilteredCourses = Collections.emptyList();

		// Reset the enrolledCourses when searching in new term
		String temp = textfieldTerm.getText();
		if (!currentTerm.equals(temp)) {
			currentTerm = temp;
			enrolledCourses.clear();
		}

    	// check if the URL is valid
    	if(!isMainURLValid()) {
    		return;
		}

    	fetch();
	}
	
	/**
	 * Handling UI and setting filters only
	 */
	@FXML
	void checkFilters(ActionEvent event) {
		isFiltering = true;
		
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
