package comp3111.coursescraper;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.text.Font;

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
	private Button buttonFilter;

	@FXML
	private CheckBox checkBoxCC;

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

	private boolean init = true;
	private String currentTerm = "";

	private enum Type {
		SEARCH,
		FILTER,
		LIST
	}

	private List<Course> unfilteredCourses = Collections.synchronizedList(new ArrayList<Course>());
	private List<Course> filteredCourses = Collections.emptyList();

	private Vector<Course> enrolledCourses = new Vector<Course>();
	private Vector<Label> enrolledSlots = new Vector<Label>();

	private ObservableList<ListEntry> listEntries = FXCollections.observableArrayList();
    
    private boolean firstClick = true;
    
    private List<String> sub_list;
    private int total_num_course = 0;  
    Task copyWorker;
    
	/**
	 * In the first click, show total number of categories in that term
	 * After the first click, scrap all the course data in that term and put into the structure of course, section, slot
	 * Also update the progress bar during the scraping process
	 * Show the total number of scrapped course in console after searching
	 */
	@FXML
	void allSubjectSearch() {
		buttonSfqEnrollCourse.setDisable(false);
		total_num_course = 0;
		unfilteredCourses = Collections.synchronizedList(new ArrayList<Course>()); 
		
    	if(firstClick) {
        	sub_list = scraper.allSubCount(textfieldURL.getText(), textfieldTerm.getText());
        	textAreaConsole.setText("Total Number of Categories/Code Prefix: " + sub_list.size());
        	
        	firstClick = false;
    	}else {

            AnchorPane ap = (AnchorPane)tabAllSubject.getContent();
            
    		ProgressBar pb = new ProgressBar(0);
        	copyWorker = createWorker(sub_list);
        	
        	
        	pb.progressProperty().unbind();
            pb.progressProperty().bind(copyWorker.progressProperty());
            pb.progressProperty().addListener(new ChangeListener<Number>(){
                @Override
                public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                    if(t1.doubleValue()==1){
                    	textAreaConsole.setText("Total Number of Courses fetched: " + total_num_course);
                    }else {
                    	textAreaConsole.setText("In Progress");
                    }
                }
            });

            new Thread(copyWorker).start();   
       
        	pb.setLayoutX(316.0);
        	pb.setLayoutY(33.0);
        	pb.setMinWidth(264.0);
        	pb.setMaxWidth(264.0);
        	pb.setMinHeight(18.0);
        	pb.setMaxHeight(18.0);
        	
        	ap.getChildren().add(pb);
        	firstClick = true;
        	
    	}
	}
	/**
	 * After all the courses in one subject is scrapped, update the system console and progress bar during the run time
	 * @param sub_list is the list of string contain all the code prefix
	 * @return Task
	 */
    public Task createWorker(List<String> sub_list) {
        return new Task() {
            @Override
            
            protected Object call() throws Exception {
            	
            	for(int i = 0; i < sub_list.size(); i++) {

            		List<Course> c = scraper.scrapeAll(textfieldURL.getText(), textfieldTerm.getText(),sub_list.get(i));
            		total_num_course += c.size();
            		unfilteredCourses.addAll(c);
            		System.out.println("SUBJECT is done");
            		Thread.sleep(200);
            		updateProgress(i+1, sub_list.size());     		
            	}

            	System.out.println("Done!");
            	System.out.println(unfilteredCourses.size());
            	
                return true;
            }
        };
    }
    
    /**
     * Search for all instructor sfq
     * And show it on the console 
     */
    @FXML
    void findInstructorSfq() {

    	List<String> temp;
    	temp = scraper.scrapeInstructorSfq(textfieldSfqUrl.getText());

    	String result = "List instructors' average SFQ: \n\n";
    	for(String s: temp) {
    		result += s + "\n";
    	}
    	textAreaConsole.setText(result);
    }

    
    /**
     * Check is enrolledCourses empty, if not search for all enrolled courses sfq and show it on the console 
     * Otherwise, will reject the command and show "There is no enrollment"
     */
    
    @FXML
    void findSfqEnrollCourse() {
    	List<String> temp;
    	
    	if(!enrolledCourses.isEmpty()) {
    		String result = "Enrolled course(s) SFQ:\n\n";
        	temp = scraper.scrapeCourseSfq(textfieldSfqUrl.getText(), enrolledCourses);
        	
        	for(String s: temp) {
        		result += s + "\n";
        	}
        	textAreaConsole.setText(result); 
    	}else {
    		textAreaConsole.setText("There is no enrollment"); 
    	}
   
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
	
	/**
	 * Get a list of unfiltered courses. Scrape if does not exist.
	 * @return A list of unfiltered coursese
	 */
	private List<Course> getListOfCourse() {
		if (unfilteredCourses.isEmpty()) {
			unfilteredCourses = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(), textfieldSubject.getText());
		}
		return unfilteredCourses;
	}
	
	/**
	 * Generate the output for textAreaConsole
	 * @param v List of courses that will be printed
	 * @param type Type of console output. Can be either SEARCH, FILTER or LIST
	 * @return A string that contains the output
	 */
    private String generateConsoleOutput(List<Course> v, Type type) {
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

		switch (type) {
			case SEARCH:
				String sectionCountOutput = "Total Number of Different sections in this search: " + sectionCount + "\n";
				String courseCountOutput = "Total Number of Courses in this search: " + courseCount + "\n";
				String instructorListOutput = "Instructors who has teaching assignment this term but does not need to teach at Tu 3:10pm: \n" + noThreeTenInstructors + "\n";
				consoleOutputResult = courseCountOutput + sectionCountOutput + instructorListOutput + catalogOutput;
				break;

			case FILTER:
				String filterResultCountOutput = "Number of courses from search that matches the filters: "	+ courseCount + "\n";
				consoleOutputResult = filterResultCountOutput + catalogOutput;
				break;

			case LIST:
				String enrollStatusOutput = "The following sections are enrolled: \n";
				consoleOutputResult = enrollStatusOutput + catalogOutput;
				break;

		}
    	return consoleOutputResult; 
    }


	/**
	 * Generate texts and set it to textAreaConsole. 
	 * Existing texts inside the console will be cleared
	 * 
	 * @param courses A list of courses
	 * @param type The type of console output. Can be either SEARCH, FILTER or LIST
	 */
	private void setConsoleOutput(List<Course> courses, Type type) {
		// reset console text
		textAreaConsole.setText("");
		textAreaConsole.setText(generateConsoleOutput(courses, type));
	}

	/**
	 * UI initialization
	 * Set up the TableColumns inside tableViewList and bind them to the data model
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

	/**
	 * Update the currently enrolled courses
	 * @param c The course that the newly enrolled/dropped section belongs to
	 * @param s The section that is newly enrolled/dropped
	 */
	void updateEnrolledCourses(Course c, Section s) {
		/**
		 * Find if the existing course exists inside enrolledCourses
		 * Create a new one (c.clone()) if not
		 */
		Course targetCourse = c.clone();
		for (Course course : enrolledCourses) {
			if (course.equals(c)) {
				targetCourse = course;
				break;
			}
		}

		Vector<Section> enrolledSections = new Vector<Section>();

		// Get a list of currently enrolled section for that course
		for (int i = 0; i < targetCourse.getNumSections(); ++i) {
			Section section = targetCourse.getSection(i);
			if (section.isEnrolled()) {
				enrolledSections.add(section);
			}
		}

		if (s.isEnrolled()) {
			enrolledSections.add(s);

			if (!enrolledCourses.contains(targetCourse)) {
				enrolledCourses.add(targetCourse);
			}

		} else {
			enrolledSections.remove(s);

			if (enrolledSections.isEmpty()) {
				enrolledCourses.remove(targetCourse);
			}
		}
		targetCourse.setSections(enrolledSections);
		targetCourse.setNumSections(enrolledSections.size());

		setConsoleOutput(enrolledCourses, Type.LIST);
		
		updateTimetable();
	}

	/**
	 * Get a list of unenrolled courses from filteredCourses. 
	 * A course is considered not enrolled if it still contains unenrolled sections
	 * 
	 * @return A list of unenrolled courses
	 */
	private List<Course> getNotEnrolledCourses() {
		Vector<Course> notEnrolled = new Vector<Course>();

		for (Course course : filteredCourses) {
			
			if (!enrolledCourses.contains(course)) {
				notEnrolled.add(course);

			} else {
				// The course exists in enrolledCourses
				Course enrolledCourse = enrolledCourses.get(enrolledCourses.indexOf(course));
				List<Section> enrolledSections = enrolledCourse.getSections();

				// Check if the course contains enrolled sections
				Vector<Section> notEnrolledSections = new Vector<Section>();

				for (int i = 0; i < course.getNumSections(); ++i) {
					Section section = course.getSection(i);

					if (!enrolledSections.contains(section)) {
						notEnrolledSections.add(section);
					}
				}
				// If the course contains unenrolled sections, add the unenrolled sections and add the course
				if (!notEnrolledSections.isEmpty()) {
					Course c = course.clone();
					c.setSections(notEnrolledSections);
					c.setNumSections(notEnrolledSections.size());

					notEnrolled.add(c);
				}
			}
		}
		return notEnrolled;
	}

	/**
	 * Add entries to be displayed in List tab
	 * @param courses List of courses to be added for displaying
	 */
	private void addListEntries(List<Course> courses) {
		for (Course course : courses) {
			for (int i = 0; i < course.getNumSections(); ++i) {
				Section section = course.getSection(i);

				// Clone the course only, as sections might change but slots won't
				listEntries.add(new ListEntry(course.clone(), section));
			}
		}
	}

	/**
	 * Handle list displaying.
	 * Update the entries to be displayed. 
	 * Existing entries will be replaced
	 */
	private void displayList() {
		if (init) {
			setUpTableView();
			ListEntry.setController(this);
			init = false;
		}

		listEntries.clear();

		addListEntries(enrolledCourses);
		addListEntries(getNotEnrolledCourses());
	}

	/**
	 * Fetch a list of course and display it in console
	 * @param filtered Whether fetching filtered courses
	 */
	private void fetch(boolean filtered) {
		// scrape using scraper and set the console output
		if (!filtered) {
			filteredCourses = getListOfCourse();
			setConsoleOutput(getListOfCourse(), Type.SEARCH);
		} else {
			filteredCourses = Filter.filterCourses(getListOfCourse());
			setConsoleOutput(filteredCourses, Type.FILTER);
		}
		displayList();
	}

	/**
	 * Search coursese according to the input terms and subject
	 */
    @FXML
    void search() {
    	buttonSfqEnrollCourse.setDisable(false);
		// Reset the unfiltered and filtered course
		unfilteredCourses.clear();
		filteredCourses.clear();

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

    	fetch(false);
	}
	
	/**
	 * Filtering courses according to the checked filters
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

		fetch(true);
	}
	
	/**
	 * This function update the course timetable
	 */
	void updateTimetable() {
		AnchorPane ap = (AnchorPane)tabTimetable.getContent();
		// remove the old slots
		for (int i = 0; i < enrolledSlots.size(); i++)
			ap.getChildren().remove(enrolledSlots.get(i));
		enrolledSlots.clear();
		
		// for all courses in the list of enrolled courses
		for (int i = 0; i < enrolledCourses.size(); i++) {
			Course c = enrolledCourses.get(i);
			
			// for all sections in this courses
			for (int j = 0; j < c.getNumSections(); j++) {
				Section sec = c.getSection(j);
				
				// find the enrolled section(s)
				if (sec.isEnrolled()) {
					
					// pick a colour for this enrolled section
					Random random = new Random();
					int red = random.nextInt(255);
			        int green = random.nextInt(255);
			        int blue = random.nextInt(255);
			        double opacity = 0.5;
		            Color randomColor = Color.rgb(red, green, blue, opacity);
					
		            // for all slots in this section
					for (int k = 0; k < sec.getNumSlots(); k++) {
						Slot s = sec.getSlot(k);
			        	
			        	double start = (double)s.getStartHour() * 20 - 140 + (double)s.getStartMinute() / 30 * 10;
			        	double end = (double)s.getEndHour() * 20 - 140 + (double)s.getEndMinute() / 30 * 10;
			        	double length = end - start;
			        	double day = (double)s.getDay() + 1;
			        	day *= 100;
			        	
			        	String [] cTitle = c.getTitle().split("-");
			        	String title = cTitle[0];
			        	if (length < 20)
			        		title += "\t" + sec.getCode();
			        	else 
			        		title += "\n" + sec.getCode();
			        	
			        	Label label = new Label(title);
			        	label.setFont(new Font("Arial", 10));
			        	
			        	/*
			        	 * Y: Each half hour is size 10 at Y axis
			        	 * Y: 0900 starts at 40 ---> 2200 starts at 300
			        	 * X: Each day is 100, so Mo => 100, Tu => 200 ...
			        	 */
			        	label.setBackground(new Background(new BackgroundFill(randomColor, CornerRadii.EMPTY, Insets.EMPTY)));
			        	label.setLayoutX(day);
			        	label.setLayoutY(start);
			        	label.setMinWidth(100.0);
			        	label.setMaxWidth(100.0);
			        	label.setMinHeight(length);
			        	label.setMaxHeight(length);
			        	ap.getChildren().addAll(label);
			        	enrolledSlots.add(label);
					}
				}
			}
		}
	}
}
