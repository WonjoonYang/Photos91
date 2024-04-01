package photos.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import photos.model.User;
import java.io.*;

public class PhotosApp extends Application{
	public static HashMap<String, User> users;
	
	public static final String storeDir = "data";
	public static final String storeFile = "users.dat";
//	static final long serialVersionUID = 1L;
	
	public static void writeUsers() throws IOException {
			ObjectOutputStream oos = new ObjectOutputStream(
			new FileOutputStream(storeDir + File.separator + storeFile));
			oos.writeObject(users);
			oos.close();
	}
	public static void readApp()
			throws IOException, ClassNotFoundException {
		try {
			ObjectInputStream ois = new ObjectInputStream(
			new FileInputStream(storeDir + File.separator + storeFile));
			users = (HashMap<String, User>)ois.readObject();
			ois.close();
		} catch(FileNotFoundException e) {
			File newDirectory = new File("./", storeDir);
			users = new HashMap<String, User>();
			User admin = new User("admin");
			users.put(admin.getUserName(), admin);
			
			if (!newDirectory.exists()) {
				boolean created = newDirectory.mkdir();
				if (created) {
	                System.out.println("data directory created.");
	                
	                writeUsers();
	            } else {
	                System.out.println("cannot create data directory");
	            }
			}else {
				writeUsers();
			}
			
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// read user data
		readApp();
		
		// create FXML loader
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/photos/view/photos.fxml"));
		
		// load fmxl, root layout manager in fxml file is GridPane
		GridPane root = (GridPane)loader.load();

		// set scene to root
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
