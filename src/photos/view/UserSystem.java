package photos.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import photos.app.PhotosApp;
import photos.model.Album;

public class UserSystem extends GridView{
	private Button createAlbumB;
	private Button deleteAlbumB;
	private Button renameAlbumB;
	private Button openAlbumB;
	private Button searchPhotoB;
	
	private ArrayList<ToggleButton> contentAlbum;
	private int selectedButton;
	public UserSystem(Stage mainStage) {
		super(mainStage);
		initUserSystem();
	}
	public UserSystem() {
		super();
		initUserSystem();
	}
	private void initUserSystem() {
		this.stage.setMinWidth(250);
		this.stage.setMinHeight(400);
		createAlbumB  = this.addButton("Create Album");
		deleteAlbumB = this.addButton("Delete Album");
		renameAlbumB = this.addButton("Rename Album");
		openAlbumB = this.addButton("Open Album");
		searchPhotoB = this.addButton("Search Photo");
		
		createAlbumB.setOnAction(event -> createAlbumHandler(event));
        deleteAlbumB.setOnAction(event -> deleteAlbumHandler(event));
        renameAlbumB.setOnAction(event ->  renameAlbumHandler(event));
        openAlbumB.setOnAction(event -> this.openAlbumHandler(event));
        searchPhotoB.setOnAction(event -> this.searchPhotoHandler(event));
        this.backB.setOnAction(event -> backHanlder(event));
        this.selectedButton=-1;
		
		displayContent();
	}
	public void backHanlder(ActionEvent e) {
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
	public void createAlbumHandler(ActionEvent e){
    	Button b = (Button)e.getSource();
		Stage mainStage = (Stage) b.getScene().getWindow();
		
		TextInputDialog dialog = new TextInputDialog("Create Album");
		dialog.initOwner(mainStage);
		dialog.setTitle("Add Album");
		dialog.setHeaderText("Enter Album Name");
		dialog.setContentText("Enter Album Name: ");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String albumName = result.get();
			boolean albumExist = false;
			for (Album album : PhotosApp.user.getAlbums()) {
				albumExist |= album.getName().equals(albumName);
			}
			if(albumExist) {
				Alert warning = new Alert(AlertType.WARNING);
				warning.initOwner(mainStage);
				warning.setTitle("Warning");
				warning.setContentText("The name \'" +albumName+ "\' is already being used.  Please try another name.");
				warning.showAndWait();
			}else {
				Album newAlbum = new Album(albumName);
				PhotosApp.user.addAlbum(newAlbum);
				displayContent();
				
				PhotosApp.writeUsers();
			}
		}
    }
	public void deleteAlbumHandler(ActionEvent e){
    	if (selectedButton == -1) {
    		
    	}else {
    		ToggleButton toggleButton = contentAlbum.get(selectedButton);
        	Album album = PhotosApp.user.getAlbums().get(selectedButton);
    		PhotosApp.user.removeAlbum(album);
    		displayContent();
        	PhotosApp.writeUsers();
    	}
    	selectedButton = -1;
    }
	public void renameAlbumHandler(ActionEvent e){
    	if (selectedButton == -1) {
    	}else {
        	
        	Button b = (Button)e.getSource();
    		Stage mainStage = (Stage) b.getScene().getWindow();
    		
    		TextInputDialog dialog = new TextInputDialog("Rename Album");
    		dialog.initOwner(mainStage);
    		dialog.setTitle("Rename Album");
    		dialog.setHeaderText("Enter Album Name");
    		dialog.setContentText("Enter Album Name: ");
    		
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				String albumName = result.get();
				boolean albumExist = false;
				for (Album album : PhotosApp.user.getAlbums()) {
					albumExist |= album.getName().equals(albumName);
				}
				if(albumExist) {
					Alert warning = new Alert(AlertType.WARNING);
					warning.initOwner(mainStage);
					warning.setTitle("Warning");
					warning.setContentText("The name \'" +albumName+ "\' is already being used.  Please try another name.");
					warning.showAndWait();
				}else {
		        	Album album = PhotosApp.user.getAlbums().get(selectedButton);
					album.changeName(albumName);
					displayContent();
					
					PhotosApp.writeUsers();
				}
			}
    	}
    }
	public void searchPhotoHandler(ActionEvent e) {
    	PhotoSearchWindow photoSearchWindow = new PhotoSearchWindow();
    	photoSearchWindow.show();
    	
    }
    public void openAlbumHandler(ActionEvent e) {
    	if (selectedButton == -1) {
    		
    	}else {
    		PhotosApp.album = PhotosApp.user.getAlbums().get(selectedButton);
    		AlbumSystem albumSystem = new AlbumSystem (); 
    		albumSystem.show();
    		stage.close();
    	}
    }
	
	private void displayContent() {
    	this.contentGridPane.getChildren().clear();
    	contentAlbum = new ArrayList<ToggleButton>();
    	int numAlbum = PhotosApp.user.getAlbums().size();
    	int numRow = numAlbum%3 == 0? numAlbum/3 : (numAlbum/3)+1;
    	
    	for (int i = 0; i < numAlbum; i++) {
    		Album album = PhotosApp.user.getAlbums().get(i);
    		
    		String display = album.getName();
    		ToggleButton toggleButton = new ToggleButton(display);
    		toggleButton.setMaxWidth(Double.MAX_VALUE);
    		toggleButton.setMaxHeight(Double.MAX_VALUE);
    		GridPane.setHalignment(toggleButton, HPos.CENTER);
    		GridPane.setValignment(toggleButton, VPos.CENTER);
    		
    		toggleButton.prefWidthProperty().bind(this.stage.widthProperty().subtract(18).divide(3));
    		toggleButton.prefHeightProperty().bind(this.stage.widthProperty().subtract(18).divide(3));
    		toggleButton.minWidthProperty().bind(toggleButton.prefWidthProperty());
    		toggleButton.minHeightProperty().bind(toggleButton.prefWidthProperty());
    		contentGridPane.prefWidthProperty().bind(this.stage.widthProperty().subtract(18));
    		contentGridPane.prefHeightProperty().bind(this.stage.widthProperty().divide(3).multiply(numRow));
    		
    		toggleButton.setOnAction(event -> {
                if (toggleButton.isSelected()) {
                    System.out.println(toggleButton.getText() + " selected");
                } else {
                    System.out.println(toggleButton.getText() + " deselected");
                }
            });
    		
    		toggleButton.setText(display);
    		contentAlbum.add(toggleButton);
    		int row = (i / 3); // Assuming 3 columns in the grid
            int col = i % 3;
            
            toggleButton.setOnMouseClicked(event -> {
            	if (toggleButton.isSelected()) {
                    // Deselect other toggle buttons
            		contentAlbum.stream()
                            .filter(toggle -> toggle != toggleButton)
                            .forEach(toggle -> {
                                ((ToggleButton) toggle).setSelected(false);
                            });
                    selectedButton = contentAlbum.indexOf(toggleButton);
                } else {
                    System.out.println(toggleButton.getText() + " deselected");
                }
            });
    		
            // Add the text area to the grid pane
            contentGridPane.add(toggleButton, col, row);
		}
    	contentGridPane.getRowConstraints().clear();
    	for(int i = 0; i < (PhotosApp.user.getAlbums().size()%3 != 0 ? (PhotosApp.user.getAlbums().size()/3+1) : PhotosApp.user.getAlbums().size()/3) ; i++) {
//    		contentGridPane.getRowConstraints().add(new RowConstraints(200));
    	}
    }
}
