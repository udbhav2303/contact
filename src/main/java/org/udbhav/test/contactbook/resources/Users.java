package org.udbhav.test.contactbook.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Users {
	
	private int user_id;
	private String auth_token;
	
	public Users(int user_id, String auth_token) {
		this.user_id = user_id;
		this.auth_token = auth_token;
	}
	
	public Users() {
		
	}
	
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getAuth_token() {
		return auth_token;
	}
	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}

}
