package application2;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	public static LoginModel loginModel = new LoginModel();
	static public MainController mainController;
	
	@FXML
	private Label isConnected;

	@FXML
	private TextField username;

	@FXML
	private TextField password;
	
	@FXML
	Button newUser;
	
	@FXML
	private TextField newUserPassword1;

	@FXML
	private TextField newUserPassword2;

	@FXML
	private Label newUserLabel;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try{
			newUser.setVisible(false);
		}catch(Exception e) {
			
		}
		
		if(loginModel.isDbConnected()) {
//			isConnected.setText("Connected");					
		} else {
			isConnected.setText("Not Connected");
		}	
		
		try {
			if(loginModel.isPasswdSet == false) {
				newUser.setVisible(true);
				newUser.setOnAction(new EventHandler <ActionEvent>() {
					public void handle(ActionEvent event) {
						try {
							newUserGUI(event);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
		}catch(Exception e) {
			
		}
	}
	
	public void newUserGUI(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();		
		FXMLLoader loader = new FXMLLoader();

		Pane root = loader.load(getClass().getResource("NewLogin.fxml").openStream());		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.setTitle("Password Manager " + Version.GetVersion());
		primaryStage.getIcons().add(new Image("img/img.png"));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public void newUser(ActionEvent event) throws Exception {
		String password = newUserPassword2.getText();
		
		if(newUserPassword1.getText().equals(password)) {
			if(true == loginModel.newUser(password)) {
				((Node)event.getSource()).getScene().getWindow().hide();
			}else {
				newUserLabel.setText("Failed");				
			}
			
		}else {
			newUserLabel.setText("Confirmation failed");
		}
	}
	
	public void Login(ActionEvent event) throws Exception{
				
		System.out.println("Login");
		
		if(loginModel.isLogin(password.getText())) {
			System.out.println("Login2");
			
			RamDB.SetAccountEntry();
			
			ResultSet resultSet;
			resultSet = loginModel.readAccountList();
			
			int index = 0;
			do {			
				if(resultSet == null) {
					break;//No entries in database
				}
				
				System.out.println("id "+ resultSet.getInt("id") + " account " + resultSet.getString("account") + " username " + resultSet.getString("username") + " password " + resultSet.getString("password"));
				RamDB.SetAccountEntry(index, resultSet.getInt("id"), resultSet.getString("account"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("others"));			
				loginModel.updateAccountId(resultSet.getInt("id"), index);
				index++;
			}while(resultSet.next());
	        

			mainController = new MainController();
			mainController.launchApplication();
			
			((Node)event.getSource()).getScene().getWindow().hide();	
			loginModel.closeConnection();
			
		}else {
			isConnected.setText("Login failed");
		}
	}
	
	public void internalLogin() throws Exception {
		int index;

		loginModel.closeConnection();
		loginModel = new LoginModel();
		
		//RamDB.SetAccountEntry();
		RamDB.ResetAccountHistory();
		
		ResultSet resultSet;
		resultSet = loginModel.readAccountList();
						
		index = 0;
		
		do {			
			if(resultSet == null) {
				break;//No entries in database
			}
			
			RamDB.SetAccountEntry(index, resultSet.getInt("id"), resultSet.getString("account"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("others"));			
			loginModel.updateAccountId(resultSet.getInt("id"), index);
			index++;
		}while(resultSet.next());
        
		mainController.MainControllerClose();
		mainController = new MainController();
		mainController.launchApplication();
		
		loginModel.closeConnection();	
	}
}
