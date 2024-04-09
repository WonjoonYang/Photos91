package photos.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import photos.app.PhotosApp;
import photos.model.Album;
import photos.model.Photo;
import photos.model.SelectableImageView;

public class SearchResult extends GridView {
    private Button createAlbumB;
    private Button captionPhotoB;
    private Button displayPhotoB;
    private Button displayAllPhotoB;
    private Button manageTagsB;
    private ArrayList<Photo> photos;

    private ArrayList<SelectableImageView> contentPhoto;
    private Button selectAllB;
	
    public SearchResult(ArrayList<Photo> photo, Stage mainStage) {
    	super(mainStage);
    	initSearchResult(photo);
    }

    public SearchResult(ArrayList<Photo> photo) {
        super();
        initSearchResult(photo);
    }
    private void initSearchResult(ArrayList<Photo> photo) {
    	this.stage.setMinWidth(250);
        this.stage.setMinHeight(400);
        this.photos = photo;
        
        createAlbumB = this.addButton("Create Album");
        captionPhotoB = this.addButton("Caption Selected Photo");
        displayPhotoB = this.addButton("Display Selected Photo");
        displayAllPhotoB = this.addButton("Display All Photo");
        manageTagsB = this.addButton("Manage Tags");
        selectAllB = this.addButton("Select/Deselect All");
        
        createAlbumB.setOnAction(event -> createAlbumHandler(event));
        captionPhotoB.setOnAction(event -> captionPhotoHandler(event));
        displayPhotoB.setOnAction(event -> this.displayPhotoHandler(event));
        displayAllPhotoB.setOnAction(event -> this.displayAllPhotoHandler(event));
        manageTagsB.setOnAction(event -> this.manageTagsHandler(event));
        selectAllB.setOnAction(event -> selectAllHanlder(event));
    	
        removeButton("Log Out");
        removeButton("Back");

        displayContent();
    }
    
	
    
	public void displayAllPhotoHandler(ActionEvent e){
		if (contentPhoto.size()>0) {
			ImageDisplayWindow imageDisplayWindow = new ImageDisplayWindow(contentPhoto);
			imageDisplayWindow.show();
		}
	}
	public void selectAllHanlder(ActionEvent e){
		boolean allSelected = true;
		for (SelectableImageView selectableImageView : contentPhoto) {
			allSelected &= selectableImageView.isSelected();
		}
		for (SelectableImageView selectableImageView : contentPhoto) {
			selectableImageView.setSelected(!allSelected);
		}
	}
    public void createAlbumHandler(ActionEvent e){
    	if (this.photos.size() == 0) {
            // Show a warning dialog indicating that more pictures are needed
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Create Album");
            alert.setHeaderText("No Pictures Found");
            alert.setContentText("You need to add pictures to create an album.");
            alert.showAndWait();
            stage.close();
            return; // Exit the method without proceeding further
        }
    	TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText("Enter the name of the new album:");
        dialog.setContentText("Album Name:");

        // Show the dialog and wait for user input
        Optional<String> result = dialog.showAndWait();

        // Process the user's input
        result.ifPresent(albumName -> {
        	if (!PhotosApp.user.isAlbumNameUnique(albumName)) {
                // Album name already exists, show an alert
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Create Album");
                alert.setHeaderText("Duplicate Album Name");
                alert.setContentText("An album with the name '" + albumName + "' already exists.");
                alert.showAndWait();
            } else {
            	Album newAlbum = new Album(photos, albumName);
            	PhotosApp.user.addAlbum(newAlbum);
            	PhotosApp.writeUsers();
            	
            	// Show success message
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Create Album");
                successAlert.setHeaderText("Album Created Successfully");
                successAlert.setContentText("Album '" + albumName + "' has been successfully created.");
                successAlert.showAndWait();
                
            }
        });
    }
	public void captionPhotoHandler(ActionEvent e){
		
		Button b = (Button)e.getSource();
		Stage mainStage = (Stage) b.getScene().getWindow();
    	// Create an instance of TagManagerWindow
		ArrayList<SelectableImageView> selectedImages = new ArrayList<SelectableImageView>(); 
    	for (SelectableImageView selectableImageView : contentPhoto) {
			if (selectableImageView.isSelected()) {
				selectedImages.add(selectableImageView);
			}
    	}
        if (selectedImages.size()>0) {
        	CaptionManagerWindow captionManagerWindow = new CaptionManagerWindow(selectedImages);
        	captionManagerWindow.show();
        	displayContent();
    	}
        
        
//    	Button b = (Button)e.getSource();
//		Stage mainStage = (Stage) b.getScene().getWindow();
//		
//    	int count = 0;
//		for (SelectableImageView selectableImageView : contentPhoto) {
//			if (selectableImageView.isSelected()) {
//				count++;
//				Image image = selectableImageView.getImage();
//				Photo photo = PhotosApp.album.searchPhotoByPath(image.getUrl());
//				
//				// Create an ImageView to display the image
//		        ImageView imageView = new ImageView(image);
//		        imageView.setFitWidth(200); // Set the width of the image view
//		        imageView.setFitHeight(200); // Set the height of the image view
//
//		        // Create a TextInputDialog
//		        TextInputDialog dialog = new TextInputDialog("Caption Photo");
//		        dialog.initOwner(mainStage);
//		        dialog.setTitle("Caption Photo");
//		        dialog.setHeaderText("Enter Caption");
//
//		        // Create a custom GridPane to include the image and the text input field
//		        GridPane grid = new GridPane();
//		        grid.add(new Label("Enter Caption:"), 0, 0);
//		        grid.add(dialog.getEditor(), 1, 0);
//		        grid.add(imageView, 0, 1, 2, 1);
//		        
//		        // Set the custom GridPane as the dialog's content
//		        dialog.getDialogPane().setContent(grid);
//
//		        // Show the dialog and wait for user interaction
//		        Optional<String> result = dialog.showAndWait();
//		        if(result.isPresent()) {
//		        	photo.captionPhoto(result.get());
//		        	PhotosApp.writeUsers();
//		        	
//		        	displayContent();
//		        }else {
//		        	break;
//		        }
//			}
//		}
//		if (count == 0) {
//			Alert warning = new Alert(AlertType.WARNING);
//			warning.initOwner(mainStage);
//			warning.setTitle("Warning");
//			warning.setContentText("Select at least one photo to add caption.");
//			warning.showAndWait();
//		}
    	
    }
	public void displayPhotoHandler(ActionEvent e) {
    	Button b = (Button)e.getSource();
		Stage mainStage = (Stage) b.getScene().getWindow();
		
		ArrayList<SelectableImageView> selectedImages = new ArrayList<SelectableImageView>(); 
    	for (SelectableImageView selectableImageView : contentPhoto) {
			if (selectableImageView.isSelected()) {
				selectedImages.add(selectableImageView);
			}
    	}
    	if (selectedImages.size()>0) {
    		ImageDisplayWindow imageDisplayWindow = new ImageDisplayWindow(selectedImages);
    		imageDisplayWindow.show();
    	}
    }
	public void manageTagsHandler(ActionEvent e) {
    	Button b = (Button)e.getSource();
		Stage mainStage = (Stage) b.getScene().getWindow();
    	// Create an instance of TagManagerWindow
		ArrayList<SelectableImageView> selectedImages = new ArrayList<SelectableImageView>(); 
    	for (SelectableImageView selectableImageView : contentPhoto) {
			if (selectableImageView.isSelected()) {
				selectedImages.add(selectableImageView);
			}
    	}
    	
        if (selectedImages.size()>0) {
        	TagManagerWindow tagManagerWindow = new TagManagerWindow(selectedImages);
        	tagManagerWindow.show();
    	}
        // Call the start method to open the window
    }
	public void movePhotoHandler(ActionEvent e){
    	ArrayList<SelectableImageView> selectedImages = new ArrayList<SelectableImageView>();
    	for (SelectableImageView selectableImageView : contentPhoto) {
			if (selectableImageView.isSelected()) {
				selectedImages.add(selectableImageView);
			}
    	}
    	if (selectedImages.size()>0) {
    		AlbumManagerWindow albumManagerWindow = new AlbumManagerWindow(selectedImages);
        	albumManagerWindow.show();
        	displayContent();
    	}
    }
	private void displayContent() {
    	contentGridPane.getChildren().clear();
    	contentPhoto = new ArrayList<SelectableImageView>();
    	int numPnoto = photos.size();
    	int numRow = numPnoto%3 == 0? numPnoto/3 : (numPnoto/3)+1;
    	for (int i = 0; i < numPnoto; i++) {
    		System.out.println("photo number: "+i);
    		Photo photo = photos.get(i);
    		
    		SelectableImageView selectableImageView = new SelectableImageView(photo);
    		ImageView imageView = selectableImageView.getImageView();


//    		contentGridPane.heightProperty().addListener((obs, oldValue, newValue) -> {
//    			System.out.println("photo number: "+"dope");
//    		    if (scrollPane.getHeight() > newValue.doubleValue()) {
//    		    	double offset = scrollPane.getHeight() - newValue.doubleValue();
//    		    	imageView.fitWidthProperty().bind(this.stage.widthProperty().subtract(Math.max(3.,18-offset)).divide(3) );
//    	    		imageView.fitHeightProperty().bind(this.stage.widthProperty().subtract(Math.max(3.,18-offset)).divide(3) );
//    	    		contentGridPane.prefWidthProperty().bind(this.stage.widthProperty().subtract(Math.max(3.,18-offset)));
//    	    		contentGridPane.maxHeightProperty().bind(this.stage.widthProperty().divide(3).multiply(numRow));
//    		    }else {
//    		    	imageView.fitWidthProperty().bind(this.stage.widthProperty().subtract(18).divide(3) );
//    	    		imageView.fitHeightProperty().bind(this.stage.widthProperty().subtract(18).divide(3) );
//    	    		contentGridPane.prefWidthProperty().bind(this.stage.widthProperty().subtract(18));
//    	    		contentGridPane.maxHeightProperty().bind(this.stage.widthProperty().divide(3).multiply(numRow));
//    		    }
//    		});
    		imageView.fitWidthProperty().bind(this.stage.widthProperty().subtract(18).divide(3) );
    		imageView.fitHeightProperty().bind(this.stage.widthProperty().subtract(18).divide(3) );
    		contentGridPane.prefWidthProperty().bind(this.stage.widthProperty().subtract(18));
//    		contentGridPane.maxHeightProperty().bind(this.stage.widthProperty().divide(3).multiply(numRow));
    		
//    		imageView.setFitWidth(190); // Set the desired width
    		imageView.setPreserveRatio(true); // Preserve the aspect ratio of the image
    		imageView.setSmooth(true); // Enable smooth scaling for better quality
    		imageView.setCache(true); // Enable image caching for better performance
    		
    		

    		Text caption = new Text(photo.getCaption());
    		caption.setFill(Color.WHITE);
    		caption.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 30));
    		selectableImageView.getChildren().add(caption);
    	    
    		GridPane.setHalignment(selectableImageView, HPos.CENTER);
    		GridPane.setValignment(selectableImageView, VPos.CENTER);
    		
    		GridPane.setHgrow(imageView, Priority.ALWAYS);
    		GridPane.setHgrow(selectableImageView, Priority.ALWAYS);
    		GridPane.setVgrow(imageView, Priority.ALWAYS);
    		GridPane.setVgrow(selectableImageView, Priority.ALWAYS);
    		
    		
    		contentPhoto.add(selectableImageView);
    		int row = (i / 3); // Assuming 3 columns in the grid
            int col = i % 3;
            // Add the text area to the grid pane
            contentGridPane.add(selectableImageView, col, row);
		}
    	contentGridPane.getRowConstraints().clear();
    	for(int i = 0; i < (PhotosApp.user.getAlbums().size()%3 != 0 ? (PhotosApp.user.getAlbums().size()/3+1) : PhotosApp.user.getAlbums().size()/3) ; i++) {
//    		gridPane.getRowConstraints().add(new RowConstraints(200));
    	}
    }
}
