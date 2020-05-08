package comp3111.coursescraper;

import java.awt.event.ActionEvent;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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

import com.gargoylesoftware.htmlunit.javascript.host.Console;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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

    private Scraper scraper = new Scraper();
    
    private boolean firstClick = true;
    
    private List<String> sub_list;
    private int total_course_num = 0;
    
    Task copyWorker;
    
    @FXML
    void allSubjectSearch() {
    	buttonSfqEnrollCourse.setDisable(false);
    	
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
                    	textAreaConsole.setText("total_course_num: "+total_course_num);
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

    	}
    	
    	
    }
    public Task createWorker(List<String> sub_list) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                // DO YOUR WORK
            	for(int i = 0; i < sub_list.size(); i++) {
            		
            		List<Course> c = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),sub_list.get(i));
            		System.out.println("SUBJECT is done");
            		total_course_num += c.size();
            		Thread.sleep(500);
            		updateProgress(i+1, sub_list.size());     		
            	}
                
                return true;
            }
        };
    }

    @FXML
    void findInstructorSfq() {
    	buttonInstructorSfq.setDisable(true);

    }

    @FXML
    void findSfqEnrollCourse() {
    	System.out.println("It works!");
    	List<String> temp = scraper.scrapeSqf(textfieldSfqUrl.getText());
    	for(String s: temp) {
    		System.out.println(s);
    	}
    		
    }

    @FXML
    void search() {
    	buttonSfqEnrollCourse.setDisable(false);
    	List<Course> v = scraper.scrape(textfieldURL.getText(), textfieldTerm.getText(),textfieldSubject.getText());
    	for (Course c : v) {
    		String newline = c.getTitle() + "\n";
    		for (int i = 0; i < c.getNumSlots(); i++) {
    			Slot t = c.getSlot(i);
    			newline += "Slot " + i + ":" + t + "\n";
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

}
