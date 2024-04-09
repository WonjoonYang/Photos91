package photos.model;

import java.io.*;
import java.util.ArrayList;

import javafx.scene.image.Image;

public class Album implements Serializable{
	ArrayList<Photo> photos;
	String name;
	public Album(ArrayList<Photo> photos, String name) {
        this.photos = photos;
        this.name = name;
    }
	public Album(String name) {
		this.name = name;
		this.photos = new ArrayList<Photo>();
	}
	public ArrayList<Photo> getPhotos(){
		return photos;
	}
	public int size() {
		return this.photos.size();
	}
	public String getName() {
		return this.name;
	}
	public void changeName(String name) {
		this.name = name;
	}
	public void addPhoto(Photo photo) {
		this.photos.add(photo);
	}
	public void removePhoto(Photo photo) {
		this.photos.remove(photo);
	}
	public Photo searchPhotoByPath(String path) {
		for(Photo photo : this.photos) {
			System.out.println(photo.getPath());
			if (photo.getPath().equals(path)) {
				return photo;
			}
		}
		System.out.println("PATH INPUT: "+path);
		return null;
	}
	public void addAllPhotos(ArrayList<Photo> newPhotos) {
        for (Photo newPhoto : newPhotos) {
            if (!photos.contains(newPhoto)) {
                photos.add(newPhoto);
            }
        }
    }
	public void removeAllPhotos(ArrayList<Photo> photosToDelete) {
        photos.removeAll(photosToDelete);
    }
}
