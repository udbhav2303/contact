package org.udbhav.test.contactbook.resources;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DBConnection {
	
	private static Connection con = null;
	private static Statement stmt = null;
	//private static PreparedStatement ps = null;
	public static Connection getConnection() {
	      if(con != null) {
	    	  return con;
	      }try {
	         //Registering the HSQLDB JDBC driver
	         Class.forName("org.hsqldb.jdbc.JDBCDriver");
	         //Creating the connection with HSQLDB
	         con = DriverManager.getConnection("jdbc:hsqldb:file:contactdb", "SA", "");
	         //To initialize the contacts_table 
	         initTables();
	         
	      }  catch (Exception e) {
	         e.printStackTrace(System.out);
	         return null;
	      }
	      return con;
    }
	
	/**
	 * @throws SQLException
	 */
	private static void initTables() throws SQLException {
		stmt = con.createStatement();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet contacts_table = meta.getTables(null, null, "CONTACTS_TABLE", null);
		ResultSet auth_table = meta.getTables(null, null, "AUTH_TABLE", null);
		if(!auth_table.next()) {
			stmt.executeUpdate("CREATE TABLE auth_table (user_id int NOT NULL, auth_token VARCHAR(100) NOT NULL, PRIMARY KEY (user_id))");
		}
		if(!contacts_table.next()) {
			stmt.executeUpdate("CREATE TABLE contacts_table (name VARCHAR(50) NOT NULL, emailId VARCHAR(50) NOT NULL, user_id int NOT NULL)");
		}
	}
	
	public static String insertContact(int pUser, String pEmailId, String pName) {
		if(pName != null && !pName.trim().isEmpty()) {
		    try {
				String sql = "SELECT * from contacts_table WHERE user_id = ? AND emailId = ?";
				String sql2 = "INSERT INTO contacts_table VALUES (?, ?, ?)";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, pUser);
				ps.setString(2, pEmailId);
				ResultSet result = ps.executeQuery();
				if(!result.next()) {
					PreparedStatement ps2 = con.prepareStatement(sql2);
					ps2.setString(1, pName);
					ps2.setString(2, pEmailId);
					ps2.setInt(3, pUser);
					int res = ps2.executeUpdate();
					con.commit();
					if(res > 0) {
						JSONObject jsonObj = new JSONObject();
						jsonObj.put("name", pName);
						jsonObj.put("emailId", pEmailId);
						return jsonObj.toJSONString();
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("errorCode", "3333");
		jsonObj.put("reason", "Email id already found with user-" + pUser + " and emailId-" + pEmailId);
		return jsonObj.toJSONString();
	}
	
	public static String fetchContact(int pUser, String pTemp, int pLimit,int pOffset) {
		try {
			String sql = "SELECT * from contacts_table WHERE user_id = ? AND (name LIKE ? OR emailId LIKE ?) LIMIT ? OFFSET ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, pUser);
			ps.setString(2, pTemp + "%");
			ps.setString(3, pTemp + "%");
			ps.setInt(4, pLimit);
			ps.setInt(5, pOffset);
			ResultSet result = ps.executeQuery();
			JSONArray jsonArray = new JSONArray();
			while(result.next()){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("name", result.getString("name"));
				jsonObj.put("emailId", result.getString("emailId"));
				jsonArray.add(jsonObj);
	        }
			if(!jsonArray.isEmpty())
				return jsonArray.toJSONString();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("errorCode", "1111");
		jsonObj.put("reason", "No result found with user-" + pUser + " and keyword-" + pTemp);
		return jsonObj.toJSONString();
	}
	
	public static String updateContact(int pUser, String pEmailId, String pName) {
		if(pEmailId != null && pName != null && !pEmailId.trim().isEmpty() && !pName.trim().isEmpty()) {
			try {
				String sql = "UPDATE contacts_table SET name = ? WHERE user_id = ? AND emailId = ?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, pName);
				ps.setInt(2, pUser);
				ps.setString(3, pEmailId);
				int res =  ps.executeUpdate();
				if(res > 0) {
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("name", pName);
					jsonObj.put("emailId", pEmailId);
					return jsonObj.toJSONString();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("errorCode", "4444");
		jsonObj.put("reason", "No Email id found with user-" + pUser + " and emailId-" + pEmailId);
		return jsonObj.toJSONString();
	}
	
	public static String deleteContact(int pUser, String pEmailId) {
		try {
			String sql = "DELETE FROM contacts_table WHERE user_id = ? AND emailId = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, pUser);
			ps.setString(2, pEmailId);
			int res = ps.executeUpdate();
			if(res > 0) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("output", pEmailId + " is deleted for user-" + pUser);
				return jsonObj.toJSONString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("errorCode", "2222");
		jsonObj.put("reason", "No result found with user-" + pUser + " and keyword-" + pEmailId);
		return jsonObj.toJSONString();
	}
	
	public static boolean checkAuth(int user_id, String auth_token) {
		if(auth_token == null || auth_token.isEmpty())
			return false;
		try {
			String sql = "SELECT * from auth_table WHERE user_id = ? AND auth_token = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setString(2, auth_token);
			ResultSet result =  ps.executeQuery();
			if(result.next() == true)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String addUser(int user_id, String auth_token) {
		try {
			String sql = "INSERT INTO auth_table VALUES (?, ?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, user_id);
			ps.setString(2, auth_token);
			int result = ps.executeUpdate();
			con.commit();
			if(result > 0) {
				JSONObject obj = new JSONObject();
				obj.put("user_id", user_id);
				obj.put("auth_token", auth_token);
				return obj.toJSONString();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("errorCode", "1000");
		jsonObj.put("reason", "Unique user_id is required");
		return jsonObj.toJSONString();
	}
	
}
