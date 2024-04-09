package photos.view;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ImageDisplayWindow {
    private Stage stage;
    private ImageView imageView;
    private int currentIndex;
    private ArrayList<SelectableImageView> imageList;
    private StackPane pane ;
    private ScrollPane tagScrollPane; // Modified
    private VBox tagVBox; // Modified
    private Text caption;
    private Text tags;

    public ImageDisplayWindow(ArrayList<SelectableImageView> imageList) {
        this.imageList = imageList;
        this.currentIndex = 0;
        this.imageView = new ImageView();
        this.imageView.setPreserveRatio(true);
        this.imageView.setFitWidth(800); // Set preferred width for image display
        this.pane = new StackPane(); // Use StackPane to allow overlaying buttons on the image
        this.pane.setPrefHeight(800); // Set the preferred height to 500
        this.tagScrollPane = new ScrollPane(); // Modified
        this.tagVBox = new VBox(); // Modified
        this.stage = new Stage();
        GridPane rootPane = new GridPane();
        
        Label previousLabel = createButtonLabel("<"); // Create custom-styled button for "Previous"
        Label nextLabel = createButtonLabel(">"); // Create custom-styled button for "Next"
        
        tagScrollPane.setContent(tagVBox); // Modified
        tagScrollPane.setFitToWidth(true); // Allow horizontal scrolling if needed
        
        pane.setAlignment(Pos.CENTER); // Align items to center horizontally
        pane.getChildren().addAll(imageView, previousLabel, nextLabel); // Add image and buttons to StackPane

        StackPane.setAlignment(previousLabel, Pos.CENTER_LEFT); // Align previous button to left
        StackPane.setAlignment(nextLabel, Pos.CENTER_RIGHT); // Align next button to right
        previousLabel.setOnMouseClicked(e -> showPreviousImage());
        nextLabel.setOnMouseClicked(e -> showNextImage());
        
        pane.prefWidthProperty().bind(rootPane.widthProperty());
        
        // Create a Text object for displaying caption
        tags = new Text("SOME TEXT");
        tags.setStyle("-fx-font-size: 14px; -fx-fill: white;"); // Example style
        
        // Add tags to VBox
        tagVBox.getChildren().add(tags);
        
        rootPane.add(pane, 0, 0); // Add pane to the first row
        rootPane.add(tagScrollPane, 0, 1); // Add scrollable pane to the second row
        // Horizontally center the imageView within its parent container
        GridPane.setHalignment(pane, HPos.CENTER);
        GridPane.setValignment(pane, VPos.CENTER);
     // Bind the prefWidth property of rootPane (GridPane) to the width of the stage
        rootPane.prefWidthProperty().bind(stage.widthProperty());
//        rootPane.minWidthProperty().bind(stage.heightProperty());
        
     // Create a ColumnConstraints object for the column
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER); // Center the content horizontally within the column

        // Apply the column constraints to the desired column index (e.g., column 0)
        rootPane.getColumnConstraints().add(columnConstraints);
        rootPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(rootPane);
        
        stage.setScene(scene);
    }
    
 // Function to create custom-styled button label
    private Label createButtonLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 50)); // Set font size and make it bold
        label.setTextFill(Color.WHITE); // Set text color to white
        label.setStyle("-fx-background-color: transparent; -fx-padding: 5px 10px; -fx-border-radius: 5px;"); // Set background transparent
        return label;
    }

    public void show() {
        showImage();
        stage.show();
    }

    private void showImage() {
        if (currentIndex >= 0 && currentIndex < imageList.size()) {
        	SelectableImageView selectableImageView = imageList.get(currentIndex);
//            imageView.setImage(selectableImageView.getImage());
            
            // Bind fitHeight of imageView to current window height minus 50
            imageView.fitHeightProperty().bind(stage.heightProperty().subtract(50));
         // Bind fitHeight of imageView to current window height minus 50
//            imageView.fitWidthProperty().bind(stage.heightProperty().subtract(50));
            
            
            // Set the image to the ImageView
            imageView.setImage(selectableImageView.getImage());
            
            pane.getChildren().remove(caption);
            caption = new Text(selectableImageView.getCaption());
    		caption.setFill(Color.WHITE);
    		caption.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 30));
    		pane.getChildren().add(caption);
    		
    		// Remove previous tags if any
    		tagVBox.getChildren().remove(tags);
            
            // Display tags below the image
            ArrayList<Tag> tagsObj = selectableImageView.getTags();
            String tagsString = "";
            for (int i = 0; i < tagsObj.size(); i++) {
                Tag tag = tagsObj.get(i);
                tagsString += tag.getTagName() + " - " + tag.getTagValue();
                if (i < tagsObj.size() - 1) {
                    tagsString += ", ";
                }
            }
            System.out.print(tagsString);
            try {
            	tags = new Text(selectableImageView.getLastModifiedTime().toString() + "\nTAGS: "+ tagsString);
                tags.setStyle("-fx-font-size: 14px; -fx-fill: black;"); // Example style
                tags.setWrappingWidth(300); // Set the width at which text should wrap
                StackPane.setAlignment(tags, Pos.BOTTOM_CENTER); // Align caption to top center
            }catch (IOException ex) {
                ex.printStackTrace();
                // Handle error loading FXML file
            }catch (URISyntaxException ex) {
            	ex.printStackTrace();
            }
            
            
            
//            tags.setFill(Color.WHITE);
//            tags.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 14));
//            tags.setLayoutY(caption.getY() + caption.getBoundsInLocal().getHeight() + 10); // Adjust 10 as per your preference
            tagVBox.getChildren().add(tags);
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
}
