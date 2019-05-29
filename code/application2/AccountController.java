package application2;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.*;

public class AccountController {
	private LoginModel loginModel = new LoginModel(); 
	private int index;
	@FXML
	TextField account;
	@FXML
	TextField username;
	@FXML
	TextField password;
	@FXML
	TextArea others;
	@FXML
	private Button del;
	@FXML
	Button updt;
	
	public void ChangeButtonName() {
		updt.setText("Add");
	}
	
	public void HideDelButton() {
		del.setVisible(false);	
	}
	
	@FXML
	public void GetAccount(int index) {		
	
		System.out.println("GetAccount index = " + index + RamDB.acc_db[index].account);
		account.setText(RamDB.acc_db[index].account);
		username.setText(RamDB.acc_db[index].username);
		password.setText(RamDB.acc_db[index].password);
		others.setText(RamDB.acc_db[index].others);
		
		this.index = index;		
	}
	
	public void AccountDelete(ActionEvent event) {
		MainController.deleteAccButton(index);
		
		//System.out.println("AccountDelete " + index + lb.getText());
		
		loginModel.deleteAccountList(index);

		RamDB.acc_db[index].account = "";
		RamDB.acc_db[index].username = "";
		RamDB.acc_db[index].password = "";
		RamDB.acc_db[index].others = "";
		
		try {
			loginModel.closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		((Node)event.getSource()).getScene().getWindow().hide();	
		
		try {
			MainController.collapseApplication();
			LoginController internalLogin = new LoginController();
			
			internalLogin.internalLogin();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
	public void AccountUpdate(ActionEvent event) {		
		RamDB.acc_db[index].account = account.getText();
		RamDB.acc_db[index].username = username.getText();
		RamDB.acc_db[index].password = password.getText();
		RamDB.acc_db[index].others = others.getText();
				
		if( false == loginModel.updateAccountList(index, account.getText(), username.getText(), password.getText(), others.getText())) {
			if(true == loginModel.insertAccountList(index, account.getText(), username.getText(), password.getText(), others.getText())) {
				MainController.addAccButton(index);
			}else {
//				System.out.println("A new button not added");
			}		
		}
		
		MainController.updateAccButton(index, RamDB.acc_db[index].account);
		
		try {
			loginModel.closeConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		((Node)event.getSource()).getScene().getWindow().hide();
	}
}
