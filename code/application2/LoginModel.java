package application2;

import java.sql.*;

public class LoginModel {
	private Connection connection;
	public boolean isPasswdSet;
	
	public LoginModel() {
		connection = SqliteConnection.Connector();
				
		isPasswdSet = false;
		
		if(connection == null) {
			System.exit(1);
		}
		
		try {
			createTables();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		isLogin("xyz");
	}	
	
	public void closeConnection() throws Exception {
		connection.close();
	}
	
	public boolean isDbConnected() {
		try {
			return !connection.isClosed();			
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean createTables() throws Exception {
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		String query = "CREATE TABLE IF NOT EXISTS login(password text)";
		
		preparedStatement = connection.prepareStatement(query);
		
		preparedStatement.executeUpdate();
		
		String query2 = "CREATE TABLE IF NOT EXISTS accountList(id integer, account text, username text, password text)";
			
		preparedStatement = connection.prepareStatement(query2);
			
		preparedStatement.executeUpdate();
				
		return true;		
	}
	
	public boolean newUser(String password) {
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		String query = "INSERT INTO login(password) VALUES(?)";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, password);
			preparedStatement.executeUpdate();
			
			return true;
							
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isLogin(String password) {
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		String query = "SELECT password from login";
		
		
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {				
				//System.out.println("isLogin " + resultSet.getString("password"));

				isPasswdSet = true;
				
				if(resultSet.getString("password").equals(password)) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}							
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean addAccountList(String account, String username, String password) {
		PreparedStatement preparedStatement;
		String query = "INSERT INTO accountList(account,username,password) VALUES(?,?,?)";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, account);
			preparedStatement.setString(2, username);
			preparedStatement.setString(3, password);
			
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;			
		}
	}
	
	public boolean deleteAccountList(int id) {
		PreparedStatement preparedStatement;
		String query = "DELETE FROM accountList WHERE id = ?";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setInt(1, id);
			
//			System.out.println("deleteAccountList()"+account+id);
			
			if(0 != preparedStatement.executeUpdate()) {			
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
//			e.printStackTrace();
			return false;			
		}
	}
	
	public boolean insertAccountList(int id, String account, String username, String password) {
		PreparedStatement preparedStatement;
		String query = "INSERT INTO accountList(id, account, username, password) VALUES(?,?,?,?)";
				
		try {
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2, account);
			preparedStatement.setString(3, username);
			preparedStatement.setString(4, password);
			
			System.out.println("insertAccountList() account "+account+" id "+id + " username "+ username + " password " + password);
			
			try{
				if(0 != preparedStatement.executeUpdate()) {
					preparedStatement.close();
					//preparedStatement.com
					return true;	
				}else {
					return false;
				}
			}catch(Exception e) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;			
		}		
	}
	
	public boolean updateAccountId(int oldId, int newId) {
		PreparedStatement preparedStatement;
		String query = "UPDATE accountList SET id = ? WHERE id = ?";
				
		try {
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setInt(1, newId);
			preparedStatement.setInt(2, oldId);
			
//			System.out.println("updateAccountList() "+account+id);
			
			try {
				if(0 != preparedStatement.executeUpdate()) {
					preparedStatement.close();
					return true;	
				}else {
					return false;
				}
			}catch(Exception e) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;			
		}
	}
	
	public boolean updateAccountList(int id, String account, String username, String password) {
		PreparedStatement preparedStatement;
		String query = "UPDATE accountList SET account = ?, username = ?, password = ? WHERE id = ?";
				
		try {
			preparedStatement = connection.prepareStatement(query);
			
			preparedStatement.setString(1, account);
			preparedStatement.setString(2, username);
			preparedStatement.setString(3, password);
			preparedStatement.setInt(4, id);
			
			System.out.println("updateAccountList() "+account+id);
			
			try {
				if(0 != preparedStatement.executeUpdate()) {
					preparedStatement.close();
					return true;	
				}else {
					return false;
				}
			}catch (Exception e) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;			
		}
	}	
	
	public ResultSet readAccountList() {
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		String query = "SELECT id,account,username,password from accountList";
		
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
					
			if(resultSet.next()) {
//				System.out.println("readAccountList() id = " + resultSet.getInt("id")); //getString("account"));

				return resultSet;
			}else {
				return null;
			}							
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
