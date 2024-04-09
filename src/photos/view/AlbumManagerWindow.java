package photos.view;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import photos.app.PhotosApp;
import photos.model.Album;
import photos.model.Photo;
import photos.model.SelectableImageView;

public class AlbumManagerWindow{
	private Stage stage;
	private ArrayList<SelectableImageView> imageList;
	private ArrayList<Photo> photoList;
	
    public AlbumManagerWindow(ArrayList<SelectableImageView> imageList) {
    	this.imageList = imageList;
    	photoList = new ArrayList<>();
        for (SelectableImageView imageView : imageList) {
            String imagePath = imageView.getImage().getUrl(); // Get the image path
            Photo photo = PhotosApp.album.searchPhotoByPath(imagePath); // Get the Photo object
            photoList.add(photo); // Add the Photo object to the list
        }

        ListView<String> albumsListView = new ListView<>();
        ObservableList<String> albumNames = FXCollections.observableArrayList();

        // Populate the album names
        for (Album album : PhotosApp.user.getAlbums()) {
            albumNames.add(album.getName());
        }
        albumsListView.setItems(albumNames);

        Button moveButton = new Button("Move");
        Button pasteButton = new Button("Paste");

        moveButton.setOnAction(e -> {
        	String selectedAlbumName = albumsListView.getSelectionModel().getSelectedItem();
            if (selectedAlbumName != null) {
                // Get the selected album from the list of albums
                Album selectedAlbum = null;
                for (Album album : PhotosApp.user.getAlbums()) {
                    if (album.getName().equals(selectedAlbumName)) {
                        selectedAlbum = album;
                        break;
                    }
                }
                selectedAlbum.addAllPhotos(photoList);
                PhotosApp.album.removeAllPhotos(photoList);
                
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setHeaderText(null);
                successAlert.setContentText(photoList.size() + " images successfully moved.");
                successAlert.showAndWait();
                
                // Close the current window
                Stage stage = (Stage) albumsListView.getScene().getWindow();
                stage.close();
            } else {
                // Handle the case where no album is selected
            }
        });

        pasteButton.setOnAction(e -> {
        	String selectedAlbumName = albumsListView.getSelectionModel().getSelectedItem();
            if (selectedAlbumName != null) {
                // Get the selected album from the list of albums
                Album selectedAlbum = null;
                for (Album album : PhotosApp.user.getAlbums()) {
                    if (album.getName().equals(selectedAlbumName)) {
                        selectedAlbum = album;
                        break;
                    }
                }
                selectedAlbum.addAllPhotos(photoList);
                
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setHeaderText(null);
                successAlert.setContentText(photoList.size() + " images successfully moved.");
                successAlert.showAndWait();
            } else {
                // Handle the case where no album is selected
            }
        });

        VBox buttonsBox = new VBox(10, moveButton, pasteButton);
        buttonsBox.setPadding(new Insets(10));
        
        Label selectionLabel;

        // Label to display the number of selected pictures
        selectionLabel = new Label(imageList.size()+" pictures selected.");

        BorderPane root = new BorderPane();
        root.setCenter(albumsListView);
        root.setBottom(new VBox(10, selectionLabel, buttonsBox));
        
        Scene scene = new Scene(root, 300, 250);
        stage = new Stage();
        stage.setTitle("Album Manager");
        stage.setScene(scene);

    }
    
    public void show() {
    	stage.showAndWait();
    }

}
