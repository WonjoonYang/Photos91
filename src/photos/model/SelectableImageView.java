package photos.model;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class SelectableImageView extends StackPane {
    private ImageView imageView;
    private boolean selected;
    private Photo photo;

    public SelectableImageView(Photo photo) {
    	this.photo=photo;
        imageView = new ImageView(new Image(photo.getPath()));
        selected = false;

        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	System.out.print("clicked: ");
                setSelected(!isSelected());
            }
        });

        getChildren().add(imageView);
    }
    
    public String getCaption() {
    	return photo.getCaption();
    }
    public ArrayList<Tag> getTags() {
    	return photo.getTags();
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        // Add visual indication for selection (e.g., border or background color)
        if (selected) {
        	System.out.println(imageView.getImage().getUrl().toString());
//        	imageView.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        	imageView.setOpacity(0.5);
        } else {
        	imageView.setOpacity(1);
        }
    }
    public ImageView getImageView() {
    	return this.imageView;
    }
    public Image getImage() {
    	return this.imageView.getImage();
    }
    public FileTime getLastModifiedTime() throws IOException, URISyntaxException {
//        Path path = Paths.get(this.photo.getPath());
    	Path path;
    	if (this.photo.getPath().startsWith("file:")) {
    		URI uri = new URI(this.photo.getPath()); 
    		path = Paths.get(uri); 
//FileSystems.getDefault().getPath(this.photo.getPath().substring(5));
//            return filePath.substring(5); // Remove the first 5 characters ("file:")
        } else {
        	path = FileSystems.getDefault().getPath(this.photo.getPath());
        }
        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        return attributes.lastModifiedTime();
    }
}