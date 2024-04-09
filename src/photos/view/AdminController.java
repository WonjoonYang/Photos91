package photos.view;

import java.io.IOException;
import java.util.Optional;
import photos.model.User;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import photos.app.PhotosApp;

public class AdminController {

	@FXML         
	ListView<String> listView;     
	@FXML
	Button removeUser;

	private ObservableList<String> obsList;              

	public void start(Stage mainStage) {                
		// create an ObservableList 
		// from an ArrayList  
		obsList = FXCollections.observableArrayList(); 
		for (String key : PhotosApp.users.keySet()) {
			obsList.add(key);
        }

		listView.setItems(obsList);
		
		listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && newValue.equals("admin")) {
				removeUser.setDisable(true);
            }else if(newValue != null) {
            	removeUser.setDisable(false);
            }
        });
		// select the first item
		listView.getSelectionModel().select(0);
	}
	
	public void addUser(ActionEvent e) throws IOException{
		Button b = (Button)e.getSource();
		Stage mainStage = (Stage) b.getScene().getWindow();
		
		TextInputDialog dialog = new TextInputDialog("ID");
		dialog.initOwner(mainStage);
		dialog.setTitle("Add User");
		dialog.setHeaderText("Enter ID");
		dialog.setContentText("Enter ID: ");
		
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String UID = result.get();
			if(PhotosApp.users.get(UID)==null) {
				User newUser = new User(UID);
				PhotosApp.users.put(UID, newUser);
				obsList.add(UID);
				
				PhotosApp.writeUsers();
			}else {
				//ID already exist.
				Alert warning = new Alert(AlertType.WARNING);
				warning.initOwner(mainStage);
				warning.setTitle("Warning");
				warning.setContentText("The ID \'" +UID+ "\' is already being used.  Please try another ID.");
				warning.showAndWait();
			}
		}
		
	}
	
	public void removeUser(ActionEvent e) throws IOException{
		String item = listView.getSelectionModel().getSelectedItem();
		PhotosApp.users.remove(item);
		obsList.remove(item);
		PhotosApp.writeUsers();
	}
}
