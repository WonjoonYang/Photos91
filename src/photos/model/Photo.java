package photos.model;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import javafx.scene.image.Image;
import java.util.ArrayList;

public class Photo implements Serializable{
	String filePath;
	String caption;
	ArrayList<Tag> tags;
	public Photo(String filePath) {
		this.filePath = filePath;
		this.tags = new ArrayList<Tag>();
	}
	public String getPath() {
		return this.filePath;
	}
	public void captionPhoto(String caption) {
		this.caption = caption;
	}
	public String getCaption() {
		return this.caption;
	}
	public ArrayList<Tag> getTags(){
		return this.tags;
	}
	public void addTag(Tag tag) {
		this.tags.add(tag);
	}
	public void removeTag(Tag tag) {
		this.tags.remove(tag);
	}
	public boolean containsTag(Tag tag) {
		return this.tags.contains(tag);
	}
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return filePath.equals(photo.filePath) &&
                caption.equals(photo.caption) &&
                tags.equals(photo.tags);
    }
	public LocalDate getLastModifiedTime() throws IOException, URISyntaxException {
		Path path;
    	if (this.filePath.startsWith("file:")) {
    		URI uri = new URI(this.filePath); 
    		path = Paths.get(uri); 
//FileSystems.getDefault().getPath(this.photo.getPath().substring(5));
//            return filePath.substring(5); // Remove the first 5 characters ("file:")
        } else {
        	path = FileSystems.getDefault().getPath(this.filePath);
        }
        BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
        
     // Convert FileTime to Instant
        Instant instant = attributes.lastModifiedTime().toInstant();
        // Convert Instant to LocalDate using system default time zone
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        
//        return attributes.lastModifiedTime();
        return localDate;
    }
}
