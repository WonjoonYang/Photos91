package photos.view;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import photos.app.PhotosApp;
import photos.model.Photo;
import photos.model.SelectableImageView;
import photos.model.Tag;

public class TagManagerWindow {
	private Stage stage;
    private ObservableList<String> tagsList;
    private ListView<String> tagsListView;
    private ArrayList<SelectableImageView> imageList;
    private ImageView imageView;
    private int currentIndex;
    private Photo currentPhoto;

    public TagManagerWindow(ArrayList<SelectableImageView> imageList) {
    	this.imageList = imageList;
    	this.currentIndex = 0;
        this.imageView = new ImageView();
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitWidth(200); // Set preferred width for image display
        this.imageView.setFitHeight(200); // Set preferred width for image display
        
        Label previousLabel = createButtonLabel("<"); // Create custom-styled button for "Previous"
        Label nextLabel = createButtonLabel(">"); // Create custom-styled button for "Next"

        StackPane layout = new StackPane(); // Use StackPane to allow overlaying buttons on the image
        layout.setAlignment(Pos.CENTER); // Align items to center horizontally
        layout.getChildren().addAll(imageView, previousLabel, nextLabel); // Add image and buttons to StackPane
        StackPane.setAlignment(previousLabel, Pos.CENTER_LEFT); // Align previous button to left
        StackPane.setAlignment(nextLabel, Pos.CENTER_RIGHT); // Align next button to right
        previousLabel.setOnMouseClicked(e -> showPreviousImage());
        nextLabel.setOnMouseClicked(e -> showNextImage());
        
        
        BorderPane root = new BorderPane();
        root.setTop(layout);
        layout.setPrefHeight(200);
        
        // Initialize the list view for tags
        tagsList = FXCollections.observableArrayList();
        tagsListView = new ListView<>(tagsList);
        root.setCenter(tagsListView);

     // Text field for adding new tag name
        TextField tagNameTextField = new TextField();
        tagNameTextField.setPromptText("Enter tag name");

        // Text field for adding new tag type
        TextField tagTypeTextField = new TextField();
        tagTypeTextField.setPromptText("Enter tag type");

        // Button to add the tag from text fields
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                addTag(tagNameTextField.getText(), tagTypeTextField.getText());
            } catch (IOException ex) {
                ex.printStackTrace(); // You can handle the exception according to your application's requirements
            }
        });

        // Button to delete the selected tag
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
        	try {
        		deleteTag();
        	}catch (IOException ex) {
                ex.printStackTrace(); // You can handle the exception according to your application's requirements
            }
        });

     // VBox for the text fields and buttons
        VBox inputBox = new VBox(10, tagNameTextField, tagTypeTextField, addButton, deleteButton);
        inputBox.setPadding(new Insets(10));
        root.setBottom(inputBox);
        
        
        Scene scene = new Scene(root, 300, 450);
        stage = new Stage();
        stage.setTitle("Tag Manager");
        stage.setScene(scene);

    }
    
    public void show() {
    	showImage();
    	showTags();
    	stage.show();
    }

    // Method to add a new tag to the list
    private void addTag(String tagName, String tagValue) throws IOException{
        if (!tagName.isEmpty() && !tagValue.isEmpty() && !tagsList.contains(tagName+" - "+tagValue)) {
        	Tag tag;
        	if((tag  = PhotosApp.user.getTag(tagName, tagValue)) == null) {
        		tag = new Tag(tagName,tagValue);
        		PhotosApp.user.addTag(tag);
        	}
            
        	currentPhoto.addTag(tag);
            tag.addPhoto(currentPhoto);
            
            tagsList.add(tagName+" - "+tagValue);
            PhotosApp.writeUsers();
        }
    }

    // Method to delete the selected tag from the list
    private void deleteTag() throws IOException{
        int selectedIndex = tagsListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	String selectedItem = tagsList.get(selectedIndex);
            String[] parts = selectedItem.split(" - ");
            
            Tag tag = PhotosApp.user.getTag(parts[0], parts[1]);
            currentPhoto.removeTag(tag);
            if(tag.getNumTaggedPhotos() == 0) {
            	PhotosApp.user.removeTag(tag);
            }
            
            tagsList.remove(selectedIndex);
            PhotosApp.writeUsers();
        }
    }
    private void showTags() {
    	tagsList.clear();
    	if (currentIndex >= 0 && currentIndex < imageList.size()) {
            for (Tag tag : this.currentPhoto.getTags()) {
            	tagsList.add(tag.toString());
            }
        }
    }
    private void showImage() {
        if (currentIndex >= 0 && currentIndex < imageList.size()) {
        	Image img = imageList.get(currentIndex).getImage();
            imageView.setImage(imageList.get(currentIndex).getImage());
            this.currentPhoto = PhotosApp.user.searchPhotoByPath(img.getUrl().toString());
        }
    }

    private void showNextImage() {
        if (currentIndex < imageList.size() - 1) {
            currentIndex++;
            showImage();
            showTags();
        }
    }

    private void showPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--;
            showImage();
            showTags();
        }
    }
    // Function to create custom-styled button label
    private Label createButtonLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 50)); // Set font size and make it bold
        label.setTextFill(Color.WHITE); // Set text color to white
        label.setStyle("-fx-background-color: transparent; -fx-padding: 5px 10px; -fx-border-radius: 5px;"); // Set background transparent
        return label;
    }


}
