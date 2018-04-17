package org.udbhav.test.contactbook.resources;

import java.sql.Connection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.udbhav.test.contactbook.resources.service.ContactsService;

@Path("/contacts")
public class ContactBookEndPoint {
	
	ContactsService contactService = new ContactsService();
	
	@GET
	@Path("/{user_id}/{temp}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getContacts(@PathParam("user_id") final int pUser,
			@PathParam("temp") final String pTemp,
			@HeaderParam("Authorization") String pAuth,
			@DefaultValue("10") @QueryParam("limit") final int pLimit,
		    @DefaultValue("0") @QueryParam("offset") final int pOffset) {
		JSONObject obj = new JSONObject();
		Connection con = DBConnection.getConnection();
		if(DBConnection.checkAuth(pUser, pAuth) == false)
			return contactService.invalidAuth();
		if(con != null)
			return DBConnection.fetchContact(pUser,pTemp,pLimit,pOffset);
		return contactService.noConnection();
	}
	
	@PUT
	@Path("/{user_id}/{emailId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateContact(Contacts contact,
			@PathParam("user_id") final int pUser,
			@PathParam("emailId") String pEmailId,
			@HeaderParam("Authorization") String pAuth) {
		Connection con = DBConnection.getConnection();
		if(DBConnection.checkAuth(pUser, pAuth) == false)
			return contactService.invalidAuth();
		if(con != null) {
			return DBConnection.updateContact(pUser,pEmailId,contact.getName());
		}
		return contactService.noConnection();
	}
	
	@POST
	@Path("/{user_id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addContact(Contacts contact,
			@PathParam("user_id") final int pUser,
			@HeaderParam("Authorization") String pAuth) {
		if(!contactService.isValidEmailId(contact.getEmailId()))
			return contactService.invalidEmail();
		Connection con = DBConnection.getConnection();
		if(DBConnection.checkAuth(pUser, pAuth) == false)
			return contactService.invalidAuth();
		if(con != null) {
			return DBConnection.insertContact(pUser,contact.getEmailId(),contact.getName());
		}
		return contactService.noConnection();
	}
	
	@DELETE
	@Path("/{user_id}/{emailId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String deleteContact(@PathParam("user_id") final int pUser,
			@PathParam("emailId") final String pEmailId,
			@HeaderParam("Authorization") String pAuth) {
		Connection con = DBConnection.getConnection();
		if(DBConnection.checkAuth(pUser, pAuth) == false)
			return contactService.invalidAuth();
		if(con != null) {
			return DBConnection.deleteContact(pUser,pEmailId);
		}
		return contactService.noConnection();
	}
	
}
