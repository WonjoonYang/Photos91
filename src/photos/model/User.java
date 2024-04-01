package photos.model;

import java.io.*;

public class User implements Serializable{
	String userName;
	public User(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	
	@Override
    public String toString() {
        return userName;
    }
}
