package application2;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

class CoreDB {
	public String account;
	public String username;
	public String password;
	public int id;
	public boolean set;
	
	CoreDB(int i) {
		account = new String("Account");
		username = new String("User Name");
		password = new String("Password");	
		set = false;
		id = i + 1;
	}
}

public class RamDB{
	public static CoreDB acc_db[] = new CoreDB[1000]; 
			
	public static void SetAccountEntry() {
		// TODO Auto-generated method stub
		for(int index = 0; index < 1000; index++) {
			acc_db[index] = new CoreDB(index);
		}
	}
	
	public static void ResetAccountHistory() {
		for(int index = 0; index < 1000; index++) {
			acc_db[index].account = new String("Account");
			acc_db[index].username = new String("User Name");
			acc_db[index].password = new String("Password");	
			acc_db[index].set = false;
			acc_db[index].id = index + 1;
		}
	}
	
	public static void SetAccountEntry(int index, int id, String account, String username, String password) {
//		System.out.println("SetAccountEntry " + index);
		
		acc_db[index].account = account;
		acc_db[index].username = username;
		acc_db[index].password = password;		
		acc_db[index].id = id;
		acc_db[index].set = true;
	}

	public static void SetAccountId(int index) {
		acc_db[index].id = index;
		acc_db[index].set = true;	
	}
}
