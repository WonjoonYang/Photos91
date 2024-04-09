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
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import photos.app.PhotosApp;
import photos.model.Photo;
import photos.model.SelectableImageView;
import photos.model.Tag;

public class CaptionManagerWindow {
	private Stage stage;
    private ListView<String> tagsListView;
    private ArrayList<SelectableImageView> imageList;
    private ImageView imageView;
    private int currentIndex;
    private Photo currentPhoto;
    private StackPane stackPane;
    private Text caption;

    public CaptionManagerWindow(ArrayList<SelectableImageView> imageList) {
    	this.imageList = imageList;
    	this.currentIndex = 0;
        this.imageView = new ImageView();
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitWidth(200); // Set preferred width for image display
        this.imageView.setFitHeight(200); // Set preferred width for image display
        
        Label previousLabel = createButtonLabel("<"); // Create custom-styled button for "Previous"
        Label nextLabel = createButtonLabel(">"); // Create custom-styled button for "Next"

        stackPane = new StackPane(); // Use StackPane to allow overlaying buttons on the image
        stackPane.setAlignment(Pos.CENTER); // Align items to center horizontally
        stackPane.getChildren().addAll(imageView, previousLabel, nextLabel); // Add image and buttons to StackPane
        StackPane.setAlignment(previousLabel, Pos.CENTER_LEFT); // Align previous button to left
        StackPane.setAlignment(nextLabel, Pos.CENTER_RIGHT); // Align next button to right
        previousLabel.setOnMouseClicked(e -> showPreviousImage());
        nextLabel.setOnMouseClicked(e -> showNextImage());
        
        BorderPane root = new BorderPane();
        root.setTop(stackPane);
        stackPane.setPrefHeight(200);

        root.setCenter(tagsListView);

     // Text field for adding new tag name
        TextField captionTextField = new TextField();
        captionTextField.setPromptText("Enter Caption");

        // Button to add the tag from text fields
        Button addButton = new Button("Add Caption");
        addButton.setOnAction(e -> {
        	setCaption(captionTextField.getText());
        });

        // Button to delete the selected tag
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
        	deleteCaption();
        });

     // VBox for the text fields and buttons
        VBox inputBox = new VBox(10, captionTextField, addButton, deleteButton);
        inputBox.setPadding(new Insets(10));
        root.setBottom(inputBox);
        
        
        Scene scene = new Scene(root, 300, 400);
        stage = new Stage();
        stage.setTitle("Caption Manager");
        stage.setScene(scene);

    }
    
    public void show() {
    	showImage();
    	stage.showAndWait();
    }

    // Method to add a new tag to the list
    private void setCaption(String caption){
        if (!caption.isEmpty()) {
        	currentPhoto.captionPhoto(caption);;
            PhotosApp.writeUsers();
        }
        showImage();
    }

    // Method to delete the selected tag from the list
    private void deleteCaption(){
    	currentPhoto.captionPhoto("");
        PhotosApp.writeUsers();
        showImage();
    }
    private void showImage() {
        if (currentIndex >= 0 && currentIndex < imageList.size()) {
        	Image img = imageList.get(currentIndex).getImage();
            imageView.setImage(imageList.get(currentIndex).getImage());
            this.currentPhoto = PhotosApp.user.searchPhotoByPath(img.getUrl().toString());
            
            stackPane.getChildren().remove(caption);
            caption = new Text(currentPhoto.getCaption());
    		caption.setFill(Color.WHITE);
    		caption.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 30));
    		stackPane.getChildren().add(caption);
        }
    }

    private void showNextImage() {
        if (currentIndex < imageList.size() - 1) {
            currentIndex++;
            showImage();
        }
    }

    private void showPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--;
            showImage();
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
