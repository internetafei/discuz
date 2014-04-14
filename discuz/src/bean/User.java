package bean;

import java.io.Serializable;

public class User implements Serializable{
	Long oldId;
	String userName;
	Long newId;
	public Long getOldId() {
		return oldId;
	}
	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getNewId() {
		return newId;
	}
	public void setNewId(Long newId) {
		this.newId = newId;
	}
	public User() {
	}
	public User(Long oldId,String userName,Long newId ) {
		  this.oldId=oldId;
		  this.userName=userName;
		  this.newId=newId;
	}
}
