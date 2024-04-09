package photos.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import photos.app.PhotosApp;

import java.io.IOException;
import javafx.scene.layout.GridPane;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PhotosController {

	@FXML Button logIn;
	@FXML TextField ID;
	
	public void convert(ActionEvent e) {
		Button b = (Button)e.getSource();
		String UID = ID.getText();
		if (UID.equals("admin")) {
			System.out.println("USER IS ADMIN");
			// Get the stage from the button's scene
			loadAdminController((Stage) b.getScene().getWindow());
		}else {
			if( (PhotosApp.user = PhotosApp.users.get(UID)) ==null) {
				System.out.println("USER ID DOES NOT EXIST.");
			}else {
				System.out.println("USER ID EXIST LOG IN SUCCESSFUL: "+PhotosApp.user);
				loadUserController((Stage) b.getScene().getWindow());
			}
		}
		System.out.println("ID: " + UID);
	}
	
	private void loadAdminController(Stage mainStage) {
		// Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/photos/view/admin.fxml"));
        try {
            // Load the root node of the FXML file
        	GridPane root = (GridPane)loader.load();

            // Get the controller instance if needed
            AdminController controller = loader.getController();
            
            Stage newWindow = new Stage();
            newWindow.initOwner(mainStage);
            newWindow.setTitle("Admin System");

            controller.start(newWindow);
            
            mainStage.close();

            // Create a new scene
            // Set the scene on the stage
            Scene scene = new Scene(root);
            newWindow.setScene(scene);
            newWindow.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle error loading FXML file
        }
    }
	
	private void loadUserController(Stage mainStage) {
		UserSystem view = new UserSystem();
		mainStage.close();
		view.show();
//		// Load the FXML file
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/photos/view/user.fxml"));
//        try {
//            // Load the root node of the FXML file
//        	GridPane root = (GridPane)loader.load();
//
//            // Get the controller instance if needed
//            UserController controller = loader.getController();
//            
//            Stage newWindow = new Stage();
//            newWindow.initOwner(mainStage);
//            newWindow.setTitle("User System");
//
//            controller.start(newWindow);
//
//            // Create a new scene
//            // Set the scene on the stage
//            Scene scene = new Scene(root);
//            newWindow.setScene(scene);
//            newWindow.show();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            // Handle error loading FXML file
//        }
    }
	
}
