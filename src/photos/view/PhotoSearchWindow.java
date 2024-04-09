package photos.view;

import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import photos.app.PhotosApp;
import photos.model.Photo;
import photos.model.Tag;

public class PhotoSearchWindow{
	private Stage stage;
    public PhotoSearchWindow() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Dropdown menu for choosing search type
        ComboBox<String> searchTypeDropdown = new ComboBox<>();
        searchTypeDropdown.getItems().addAll("Search by Date Range", "Search by Tag");
        searchTypeDropdown.setValue("Search by Date Range");
//        root.setTop(searchTypeDropdown);

        // GridPane for inputs
        GridPane inputGrid = new GridPane();
        inputGrid.setVgap(10);
        inputGrid.setHgap(10);

        // Date pickers for date range search
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        // Text inputs and controls for tag search
        TextField tagInput1 = new TextField();
        TextField tagInput2 = new TextField();
        TextField tagInput3 = new TextField();
        TextField tagInput4 = new TextField();
        ComboBox<String> operatorDropdown = new ComboBox<>();
        operatorDropdown.getItems().addAll("AND", "OR");
        operatorDropdown.setValue("AND");
        CheckBox combineTagsCheckbox = new CheckBox("Combine Tags");
        combineTagsCheckbox.setSelected(true);

        // Add components to grid
        inputGrid.addRow(0, searchTypeDropdown);
        inputGrid.addRow(1, new Label("Start Date:"), startDatePicker);
        inputGrid.addRow(2, new Label("End Date:"), endDatePicker);
        inputGrid.addRow(3, new Label("Tag1 Name:"), tagInput1);
        inputGrid.addRow(4, new Label("Tag1 Value:"), tagInput2);
        inputGrid.addRow(5, new Label("Tag2 Name:"), tagInput3);
        inputGrid.addRow(6, new Label("Tag2 Value:"), tagInput4);
        inputGrid.addRow(7, new Label("Operator:"), operatorDropdown);
        inputGrid.addRow(8, combineTagsCheckbox);
        
        startDatePicker.disableProperty().bind(searchTypeDropdown.valueProperty().isEqualTo("Search by Tag"));
        endDatePicker.disableProperty().bind(searchTypeDropdown.valueProperty().isEqualTo("Search by Tag"));
        
        tagInput1.disableProperty().bind(searchTypeDropdown.valueProperty().isEqualTo("Search by Date Range") );
        tagInput2.disableProperty().bind(searchTypeDropdown.valueProperty().isEqualTo("Search by Date Range") );
        
        tagInput3.disableProperty().bind(
        	    combineTagsCheckbox.selectedProperty().not().or(
        	        searchTypeDropdown.valueProperty().isEqualTo("Search by Date Range")
        	    )
        	);
        	tagInput4.disableProperty().bind(
        	    combineTagsCheckbox.selectedProperty().not().or(
        	        searchTypeDropdown.valueProperty().isEqualTo("Search by Date Range")
        	    )
        	);
        	
        operatorDropdown.disableProperty().bind(searchTypeDropdown.valueProperty().isEqualTo("Search by Date Range").or(combineTagsCheckbox.selectedProperty().not()));
        combineTagsCheckbox.disableProperty().bind(searchTypeDropdown.valueProperty().isEqualTo("Search by Date Range") );
        
        startDatePicker.setOnAction(event -> {
            LocalDate startDate = startDatePicker.getValue();
            if (startDate != null) {
                endDatePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(date.isBefore(startDate));
                    }
                });
            } else {
                endDatePicker.setDayCellFactory(null);
            }
        });

        endDatePicker.setOnAction(event -> {
            LocalDate endDate = endDatePicker.getValue();
            if (endDate != null) {
                startDatePicker.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        setDisable(date.isAfter(endDate));
                    }
                });
            } else {
                startDatePicker.setDayCellFactory(null);
            }
        });

        // Add input grid to root
        root.setCenter(inputGrid);

        // Event handler for search type dropdown
        searchTypeDropdown.setOnAction(event -> {
            String selected = searchTypeDropdown.getSelectionModel().getSelectedItem();
            if (selected != null) {
                if (selected.equals("Search by Date Range")) {

                } else if (selected.equals("Search by Tag")) {

                }
            }
        });
        
        Button searchButton = new Button("Search");
        inputGrid.add(searchButton, 1, 9); // Add search button to the bottom right of the grid

        // Align the search button to the right
        GridPane.setHalignment(searchButton, javafx.geometry.HPos.RIGHT);
//        GridPane.setHalignment(searchTypeDropdown, javafx.geometry.HPos.CENTER);

        // Event handler for the search button
        searchButton.setOnAction(event -> {
        	if (searchTypeDropdown.getSelectionModel().getSelectedItem().equals("Search by Date Range")) {
        	    // Get the selected start date
        	    LocalDate startDate = startDatePicker.getValue();

        	    // Get the selected end date
        	    LocalDate endDate = endDatePicker.getValue();
        	    
        	    if (startDate != null && endDate != null) {
                    ArrayList<Photo> photosWithinDateRange = PhotosApp.user.getPhotosWithinDateRange(startDate, endDate);
                    SearchResult searchResult = new SearchResult(photosWithinDateRange); 
                    searchResult.show();
                } else {
                    // Handle case where start date or end date is not selected
                    System.out.println("Please select both start and end dates.");
                }
        	}else {
        		if (combineTagsCheckbox.isSelected()) {
        	        if (!tagInput1.getText().isEmpty() && !tagInput2.getText().isEmpty() && 
        	            !tagInput3.getText().isEmpty() && !tagInput4.getText().isEmpty()) {
        	            // Get inputs from text fields
        	            String input1 = tagInput1.getText();
        	            String input2 = tagInput2.getText();
        	            String input3 = tagInput3.getText();
        	            String input4 = tagInput4.getText();
        	            
        	            // Use PhotosApp.user.getTag(input1, input2) to get each tag
        	            Tag tag1 = PhotosApp.user.getTag(input1, input2);
        	            Tag tag2 = PhotosApp.user.getTag(input3, input4);
        	            // Get the selected operator from the dropdown
        	            String selectedOperator = operatorDropdown.getValue();

        	            // Create SearchResult based on the selected operator using ternary operator
        	            SearchResult searchResult = (selectedOperator.equals("AND")) ?
        	                    new SearchResult(Tag.tagAnd(tag1, tag2)) :
        	                    new SearchResult(Tag.tagOr(tag1, tag2));
                        searchResult.show();
                        stage.close();
        	        } else {
        	            // Handle case where all tag inputs are not filled
        	            System.out.println("Please fill all tag inputs.");
        	        }
        		}else {
        	        if (!tagInput1.getText().isEmpty() && !tagInput2.getText().isEmpty()) {
        	            // Get inputs from text fields
        	            String input1 = tagInput1.getText();
        	            String input2 = tagInput2.getText();
        	            
        	            // Use PhotosApp.user.getTag(input1, input2) to get each tag
        	            Tag tag1 = PhotosApp.user.getTag(input1, input2);
                        SearchResult searchResult = new SearchResult(tag1.getPhotos()); 
                        searchResult.show();
                        stage.close();
        	        } else {
        	            // Handle case where tag inputs are not filled
        	            System.out.println("Please fill both tag inputs.");
        	        }
        	    }
        		Tag tag = PhotosApp.user.getTag(null, null);
        	}
        	
            // Add your search logic here
            System.out.println("Search button clicked!");
        });

        // Add input grid to root
        root.setCenter(inputGrid);

        // Scene setup
        Scene scene = new Scene(root, 400, 400);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Photo Search");
    }
    public void show() {
    	stage.show();
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}
