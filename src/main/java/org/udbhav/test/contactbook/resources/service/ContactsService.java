package org.udbhav.test.contactbook.resources.service;

import java.util.regex.Pattern;

import org.json.simple.JSONObject;

public class ContactsService {

	public boolean isValidEmailId(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                            "[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                            "A-Z]{2,7}$";
                             
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
	
	public String invalidEmail() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("errorCode", "1234");
		jsonObj.put("reason", "Email id format is incorrect");
		return jsonObj.toJSONString();
	}
	
	public String invalidAuth() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("errorCode", "1234");
		jsonObj.put("reason", "Invalid Authentication");
		return jsonObj.toJSONString();
	}
	
}
