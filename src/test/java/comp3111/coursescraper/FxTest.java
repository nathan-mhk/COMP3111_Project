/**
 * 
 * You might want to uncomment the following code to learn testFX. Sorry, no tutorial session on this.
 * 
 */
package comp3111.coursescraper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit.ApplicationTest;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.fxml.FXMLLoader;

public class FxTest extends ApplicationTest {

	private Scene s;
	
	@Override
	public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("/ui.fxml"));
   		VBox root = (VBox) loader.load();
   		Scene scene =  new Scene(root);
   		stage.setScene(scene);
   		stage.setTitle("Course Scraper");
   		stage.show();
   		s = scene;
	}

	
	@Test
	public void testButton() {
		clickOn("#tabSfq");
		clickOn("#buttonInstructorSfq");
		Button b = (Button)s.lookup("#buttonInstructorSfq");
		sleep(1000);
		assertTrue(b.isDisabled());
		
		clickOn("#tabMain");
		
		// test url
		clickOn("#textfieldURL");
		moveTo("#textfieldTerm");
		type(KeyCode.END); 
		type(KeyCode.BACK_SPACE, 8); 
		clickOn("#buttonSearch");
		sleep(500);
		
		// test subject
		clickOn("#textfieldSubject");
		type(KeyCode.BACK_SPACE, 4); 
		write("c");
		clickOn("#buttonSearch");
		clickOn("#textfieldSubject");
		type(KeyCode.BACK_SPACE, 4); 
		write("comp");
		clickOn("#buttonSearch");
		sleep(500);
		
		// test term
		clickOn("#textfieldTerm");
		type(KeyCode.BACK_SPACE, 4); 
		write("19");
		clickOn("#buttonSearch");
		clickOn("#textfieldTerm");
		type(KeyCode.BACK_SPACE, 4); 
		write("191a");
		clickOn("#buttonSearch");
		sleep(500);
		
		// back to default
		clickOn("#textfieldURL");
		moveTo("#textfieldTerm");
		type(KeyCode.END); 
		write("cgi-bin/");
		clickOn("#textfieldTerm");
		type(KeyCode.BACK_SPACE, 4); 
		write("1910");
		clickOn("#textfieldSubject");
		type(KeyCode.BACK_SPACE, 4); 
		write("COMP");
		clickOn("#buttonSearch");
		sleep(500);

		clickOn("#tabFilter");
		clickOn("#buttonFilter");
		clickOn("#buttonFilter");
		clickOn("#checkBoxCC");
		clickOn("#tabList");

		/**
		 * TODO:
		 * Click on check boxes in List tab
		 */
	
		clickOn("#tabTimetable");
		sleep(1000);
	}
}
