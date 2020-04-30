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

import java.util.Random;
import java.util.List;
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
    	
    	List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    	
    	// loop all courses in this list
    	for (Course c : v) {
    		String newline = c.getTitle() + "\n";
    		
    		// loop all sections in this course
    		for (int i = 0; i < c.getNumSections(); i++) {
    			Section sec = c.getSection(i);
    			newline += "\tSection: " + sec.getCode() + " (" + sec.getID() + ")\n";
    			
    			// loop all slots in this section
    			for (int j = 0; j < sec.getNumSlots(); ) {
        			Slot t = sec.getSlot(j);
        			j += 1;
        			newline += "\t\tSlot " + j + ": " + t + "\n";
        		}
    		}
    		
    		textAreaConsole.setText(textAreaConsole.getText() + "\n" + newline);
    	}
    	
    	//Add a random block on Saturday
    	AnchorPane ap = (AnchorPane)tabTimetable.getContent();
    	Label randomLabel = new Label("COMP1022\nL1");
    	Random r = new Random();
    	double start = (r.nextInt(10) + 1) * 20 + 40;

    	randomLabel.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    	randomLabel.setLayoutX(600.0);
    	randomLabel.setLayoutY(start);
    	randomLabel.setMinWidth(100.0);
    	randomLabel.setMaxWidth(100.0);
    	randomLabel.setMinHeight(60);
    	randomLabel.setMaxHeight(60);
    
    	ap.getChildren().addAll(randomLabel);
    	
    	
    	
    }

	private void clearConsoleOutput() {
		textAreaConsole.setText("");
	}

	/**
	 * Generate texts and set it to textAreaConsole
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
			setConsoleOutput(Filter.filterCourse(getListOfCourse()));
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
	 *  Handling UI and setting fiteres only
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
