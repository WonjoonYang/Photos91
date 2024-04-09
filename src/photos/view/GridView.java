package photos.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GridView{
	public GridPane contentGridPane;
	public ArrayList<Button> buttons;
	public int numButtons;
	public GridPane buttonsGridPane;
	public Stage stage;
	public ScrollPane scrollPane;
	public Scene scene;
	public Button logOutB;
	public Button backB;
	
	public GridView(Stage mainStage) {
		this.stage = mainStage;
		this.init();
	}
    public GridView() {
    	this.stage = new Stage();
    	this.init();
    }
    private void init(){
    	this.contentGridPane = new GridPane();
    	this.buttons = new ArrayList<Button>();
    	this.numButtons = 0;
    	this.buttonsGridPane = new GridPane();
    	this.scrollPane = new ScrollPane();
        // Root GridPane
        GridPane root = new GridPane();
        root.setPrefSize(650, 400);

        // Column Constraints
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        column1.setMinWidth(10);
        column1.setPrefWidth(650);

        // Row Constraints
        RowConstraints row1 = new RowConstraints();
        row1.setMinHeight(10);
        row1.setPrefHeight(100);
        row1.setMaxHeight(Double.MAX_VALUE);

        RowConstraints row2 = new RowConstraints();
        row2.setMinHeight(10);
//        row2.setPrefHeight(350);
        row2.setMaxHeight(Double.MAX_VALUE);
        
        root.getRowConstraints().addAll(row1,row2);

        // ScrollPane
        scrollPane.prefWidthProperty().bind(this.stage.widthProperty());
        scrollPane.maxHeightProperty().bind(this.stage.heightProperty().subtract(buttonsGridPane.heightProperty()));
        scrollPane.minHeightProperty().bind(this.stage.heightProperty().subtract(buttonsGridPane.heightProperty()));

        // Content GridPane inside ScrollPane
        contentGridPane.setAlignment(javafx.geometry.Pos.CENTER);

        // Column Constraints for content GridPane
        ColumnConstraints contentColumn1 = new ColumnConstraints();
        contentColumn1.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        contentColumn1.setMinWidth(10);
        contentColumn1.setPrefWidth(200);

        ColumnConstraints contentColumn2 = new ColumnConstraints();
        contentColumn2.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        contentColumn2.setMinWidth(10);
        contentColumn2.setPrefWidth(200);

        ColumnConstraints contentColumn3 = new ColumnConstraints();
        contentColumn3.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
        contentColumn3.setMinWidth(10);
        contentColumn3.setPrefWidth(200);

        // Add Column Constraints to content GridPane
        contentGridPane.getColumnConstraints().addAll(contentColumn1, contentColumn2, contentColumn3);

        // Add content GridPane to ScrollPane
        scrollPane.setContent(contentGridPane);
        
        logOutB  = this.addButton("Log Out");
		backB = this.addButton("Back");
		
		logOutB.setOnAction(event -> logOutHanlder(event));


        // Add ScrollPane and Buttons GridPane to Root GridPane
        root.add(scrollPane, 1, 1);
        root.add(buttonsGridPane, 1, 0);

        // Set Insets for Root GridPane
        root.setMargin(scrollPane, new Insets(0));
        root.setMargin(buttonsGridPane, new Insets(0));

        // Create Scene and set to Stage
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("JavaFX Layout Example");
    }
    
    public void logOutHanlder(ActionEvent e) {
    	// create FXML loader
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/src/photos/view/photos.fxml"));
		
		// load fmxl, root layout manager in fxml file is GridPane
		GridPane root;
		try {
			root = (GridPane)loader.load();
			Scene scene = new Scene(root);
			Stage logIn = new Stage();
			logIn.setScene(scene);
			logIn.setResizable(false);
			logIn.show();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// set scene to root
    	stage.close();
    }
    
    public Button addButton(String buttonName) {
    	
//    	ColumnConstraints buttonColumn = new ColumnConstraints();
//        buttonColumn.setHgrow(javafx.scene.layout.Priority.SOMETIMES);
//        buttonColumn.setMinWidth(10);
//        buttonColumn.setPrefWidth(130);
//        buttonsGridPane.getColumnConstraints().add(buttonColumn);
        
        if(numButtons%5==0) {
        	RowConstraints buttonRow = new RowConstraints();
            buttonRow.setMinHeight(10);
            buttonRow.setPrefHeight(30);
            buttonRow.setMaxHeight(Double.MAX_VALUE);
            buttonsGridPane.getRowConstraints().add(buttonRow);
        }
        
        
    	Button button = new Button(buttonName);
    	buttons.add(button);
    	buttonsGridPane.add(button, numButtons%5, numButtons/5);
    	button.prefWidthProperty().bind(this.stage.widthProperty().divide(5));
    	GridPane.setHalignment(button, HPos.CENTER);
    	
    	numButtons++;
    	
    	return button;
    }
    public void removeButton(String buttonName) {
        // Remove button from the buttons array
        Button buttonToRemove = null;
        for (Button button : buttons) {
            if (button.getText().equals(buttonName)) {
                buttonToRemove = button;
                break;
            }
        }
        if (buttonToRemove != null) {
            buttons.remove(buttonToRemove);
        }

        // Clear buttons from buttonsGridPane
        buttonsGridPane.getChildren().clear();

        // Re-add buttons to buttonsGridPane
        int row = 0;
        int col = 0;
        for (Button button : buttons) {
            buttonsGridPane.add(button, col, row);
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }
    }
    public void show() {
    	stage.show();
    }

//    public static void main(String[] args) {
//    	GridView view = new GridView(); 
//    	view.show();
//    }
}
