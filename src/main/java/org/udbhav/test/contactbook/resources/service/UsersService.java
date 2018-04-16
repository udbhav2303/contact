package org.udbhav.test.contactbook.resources.service;

import java.util.UUID;

import org.json.simple.JSONObject;

public class UsersService {
	
	public String generateToken() {
		String auth_token = UUID.randomUUID().toString();
        return auth_token;
	}
	
	public String invalidAuth() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("errorCode", "1234");
		jsonObj.put("reason", "Invalid Authentication");
		return jsonObj.toJSONString();
	}
}
