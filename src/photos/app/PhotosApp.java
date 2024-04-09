package photos.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.function.Function;

import photos.model.Album;
import photos.model.Photo;
import photos.model.User;
import java.io.*;
import java.nio.file.Paths;

public class PhotosApp extends Application{
	public static HashMap<String, User> users;
	public static User user;
	public static Album album;
	
	public static final String storeDir = "data";
	public static final String storeFile = "users.dat";
//	static final long serialVersionUID = 1L;
	
	public static void writeUsers(){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(
			new FileOutputStream(storeDir + File.separator + storeFile));
			oos.writeObject(users);
			oos.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
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
			
			User stock = new User("stock");
			users.put(stock.getUserName(), stock);
			Album album = new Album("stock");
			stock.addAlbum(album);
			
			Function<String, Photo> f = fileName-> {
				String relativePath = storeDir + java.io.File.separator + fileName;
		        // Convert relative path to absolute path
		        String absolutePath = Paths.get(relativePath).toAbsolutePath().toUri().toString();
		        
		        absolutePath = absolutePath.startsWith("file:///")? absolutePath.replaceFirst("file:///", "file:/") : absolutePath;
		        
				Photo photo = new Photo(absolutePath);
				return new Photo(absolutePath);
			};
			for (int i =1; i<10; i++) {
				Photo photo = f.apply("img_"+i+".jpg");
				photo.captionPhoto("CAPTION "+ i);
				album.addPhoto(photo);
				stock.addPhoto(photo);
			}
			
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
		loader.setLocation(getClass().getResource("/src/photos/view/photos.fxml"));
		
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
