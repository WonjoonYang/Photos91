package photos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Tag implements Serializable{
	String tagName;
	String tagValue;
	ArrayList<Photo> photos;
	
	public Tag(String tagName, String tagValue) {
		this.tagName = tagName;
		this.tagValue = tagValue;
		this.photos = new ArrayList<Photo>();
	}
	
	public String toString() {
		return tagName+" - "+tagValue;
	}
	public String getTagName() {
		return this.tagName;
	}
	public String getTagValue() {
		return this.tagValue;
	}
	public void addPhoto(Photo photo) {
		this.photos.add(photo);
	}
	public ArrayList<Photo> getPhotos() {
		return this.photos;
	}
	public int getNumTaggedPhotos() {
		return photos.size();
	}
	public static ArrayList<Photo> tagAnd(Tag tag1, Tag tag2) {
		ArrayList<Photo> resultPhotos = new ArrayList<Photo>();
        for (Photo photo : tag1.getPhotos()) {
            if (photo.containsTag(tag2)) {
                resultPhotos.add(photo);
            }
        }
        return resultPhotos;
    }
	public static ArrayList<Photo> tagOr(Tag tag1, Tag tag2) {
		ArrayList<Photo> resultPhotos = new ArrayList<Photo>();
		ArrayList<Photo> tag2NotTag1 = tag2.getPhotos().stream()
				.filter(p->!p.containsTag(tag1))
				.collect(Collectors.toCollection(ArrayList::new));;
		
		resultPhotos.addAll(tag1.getPhotos());
		resultPhotos.addAll(tag2NotTag1);
        return resultPhotos;
    }
}
