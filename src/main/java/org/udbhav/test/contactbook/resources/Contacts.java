package org.udbhav.test.contactbook.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Contacts {
	
	private String name;
	private String emailId;
	
	public Contacts(String name, String emailId) {
		this.name = name;
		this.emailId = emailId;
	}
	
	public Contacts() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
