package org.udbhav.test.contactbook.resources;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.udbhav.test.contactbook.resources.service.UsersService;

@Path("/auth")
public class AuthUserEndoint {
	
	UsersService userService = new UsersService();
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addUser(Users user,
			@HeaderParam("Authorization") String pAuth) {
		if(pAuth == null || pAuth.isEmpty() || !pAuth.equals("abcd1234"))
			return userService.invalidAuth();
		Connection con = DBConnection.getConnection();
		String auth_token = user.getAuth_token();
		int user_id = user.getUser_id();
		if(con != null) {
			if(user_id == 0)
				user_id = (int) (Math.random() * 10000);
			if(auth_token == null || auth_token.isEmpty())
				auth_token = userService.generateToken();
			return DBConnection.addUser(user_id,auth_token);
		}
		return userService.noConnection();
	}

}
