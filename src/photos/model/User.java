package photos.model;

import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable{
	String userName;
	ArrayList<Album> albums;
	ArrayList<Photo> photos;
//	ArrayList<Tag> tags;
	HashList tags;
	private class HashList implements Serializable {
	    private HashMap<String, ArrayList<Tag>> hashList;

	    public HashList() {
	        hashList = new HashMap<>();
	    }

	    // Method to add a value to the map
	    public void put(String key, Tag value) {
	        if (!hashList.containsKey(key)) {
	            hashList.put(key, new ArrayList<>());
	        }
	        hashList.get(key).add(value);
	    }

	    // Method to remove a value from the map
	    public void remove(String key, Tag value) {
	        if (hashList.containsKey(key)) {
	            hashList.get(key).remove(value);
	            if (hashList.get(key).isEmpty()) {
	                hashList.remove(key);
	            }
	        }
	    }

	    // Method to get all values associated with a key
	    public List<Tag> get(String key) {
	        return hashList.getOrDefault(key, new ArrayList<>());
	    }

	    // Other methods as needed
	}

	public User(String userName) {
		this.userName = userName;
		this.albums = new ArrayList<Album>();
		this.photos = new ArrayList<Photo>();
		this.tags = new HashList();
	}
	public String getUserName() {
		return userName;
	}
	public ArrayList<Album> getAlbums() {
		return albums;
	}
	public Album getAlbumByName(String name) {
		for (Album album : this.albums) {
			if (album.getName().equals(name)) {
				return album;
			}
		}
		return null;
	}
	public void addPhoto(Photo photo) {
		this.photos.add(photo);
	}
	public void removePhoto(Photo photo) {
		this.photos.remove(photo);
	}
	public void addAlbum(Album album) {
		this.albums.add(album);
	}
	public void removeAlbum(Album album) {
		this.albums.remove(album);
	}
	public void addTag(Tag tag) {
		this.tags.put(tag.getTagName(),tag);
	}
	public void removeTag(Tag tag) {
		this.tags.remove(tag.getTagName(),tag);
	}
	public Tag getTag(String tagName, String tagValue) {
		for (Tag tag : tags.get(tagName)) {
			if (tag.getTagValue().equals(tagValue)) {
				return tag;
			}
		}
		return null;
	}
	public Photo searchPhotoByPath(String path) {
		for(Photo photo : this.photos) {
			if (photo.getPath().equals(path)) {
				return photo;
			}
		}
		return null;
	}
	public void removeAlbumByName(String name) {
		ArrayList<Album> newAlbum = new ArrayList<Album>();
		for (Album album : albums) {
			if (!album.getName().equals(name)) {
				newAlbum.add(album);
			}
		}
		albums = newAlbum;
	}
	public boolean isAlbumNameUnique(String input) {
        for (Album album : albums) {
            if (album.getName().equals(input)) {
                // Album name already exists
                return false;
            }
        }
        // Album name is unique
        return true;
    }
	public ArrayList<Photo> getPhotosWithinDateRange(LocalDate startDate, LocalDate endDate) {
        ArrayList<Photo> photosWithinRange = new ArrayList<>();
        for (Photo photo : photos) {
        	try {
	            LocalDate lastModifiedTime = photo.getLastModifiedTime();
	            if (lastModifiedTime != null && !lastModifiedTime.isBefore(startDate) && !lastModifiedTime.isAfter(endDate)) {
	                photosWithinRange.add(photo);
	            }
        	}catch (IOException e) {
        		e.printStackTrace();
        	}catch (URISyntaxException e){
        		e.printStackTrace();
        	}
        }
        return photosWithinRange;
    }
	
	@Override
    public String toString() {
        return userName;
    }
}
