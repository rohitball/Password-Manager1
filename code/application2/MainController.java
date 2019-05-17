package application2;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainController {
		
	private static ArrayList<Button> buttons = new ArrayList<Button>(5);
	static Button child2_bt1 = new Button("New Account + ");
	static AnchorPane child1_sp_content_ap;
	static double layout_y;
	
	private static Stage primaryStage;
		
	public void MainControllerClose() {
		buttons.clear();
	}
	
	private void SetFirstButton() {
		child2_bt1.setLayoutX(50);
		child2_bt1.setLayoutY(700);
		child2_bt1.setPrefHeight(80);
		child2_bt1.setPrefWidth(400);
		child2_bt1.setLineSpacing(100);
		child2_bt1.setStyle("-fx-font-size: 2em; ");		
		
		child2_bt1.setOnAction(new EventHandler <ActionEvent>() {
			public void handle(ActionEvent event) {
				Stage secondaryStage = new Stage();
				
				FXMLLoader loader = new FXMLLoader();
				
				try {
				Pane root = loader.load(getClass().getResource("Account.fxml").openStream());
				
				secondaryStage.initStyle(StageStyle.UTILITY);
				secondaryStage.initModality(Modality.WINDOW_MODAL);
				secondaryStage.initOwner(primaryStage);
				
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				secondaryStage.setScene(scene);
				secondaryStage.setTitle("Password Manager " + Version.GetVersion());
				
				AccountController accountController = (AccountController)loader.getController();
				accountController.GetAccount(buttons.size());	
				//For a new entry delete button shouldn't appear
				accountController.HideDelButton();
				accountController.ChangeButtonName();
				
				secondaryStage.show();
				} catch(Exception e) {
		//			e.fillInStackTrace("df");
				}
								
				//Add a new entry in ram database
				RamDB.SetAccountId(buttons.size());
			}
		});		
	}
	
	static public void addAccButton(int index) {
		configureAccButton(index);		
	}
	
	static public void deleteAccButton(int index) {
		buttons.get(index).setVisible(false);
	}

	static public void updateAccButton(int index, String account) {
		try {		
			buttons.get(index).setText(account);
		}catch(Exception e) {
			
		}
	}

	private static void configureAccButton(int index) {
		
		System.out.println("configureAccButton " + index );
		
		try {
			buttons.add(new Button(RamDB.acc_db[index].account));
		}catch(Exception e) {
			buttons.add(new Button(""));
		}
		
		buttons.get(buttons.size() - 1).setLayoutX(50);
		buttons.get(buttons.size() - 1).setPrefHeight(80);
		buttons.get(buttons.size() - 1).setPrefWidth(400);
		buttons.get(buttons.size() - 1).setLineSpacing(100);		
		buttons.get(buttons.size() - 1).setId(Integer.toString(index));
				
		buttons.get(buttons.size() - 1).setOnAction(new EventHandler <ActionEvent>() {
			public void handle(ActionEvent event) {
				try{
					OpenAccount(event);
				}catch(Exception e) {
					
				}
			}
		});
		
		child1_sp_content_ap.getChildren().add(buttons.get(buttons.size() - 1));
		layout_y += 100.0;
		AnchorPane.setTopAnchor(buttons.get(buttons.size() - 1), layout_y);	
	}
	
	static public void collapseApplication() {
		primaryStage.hide();
	}
	
	public void launchApplication() {
		//Show the application main window
		primaryStage.show();
	}
		
	public MainController() {
		primaryStage = new Stage();
				
		VBox root = new VBox();
		Scene scene = new Scene(root, 500, 700);
		
		//Child 1 - scroll pane
		ScrollPane child1_sp = new ScrollPane();
		child1_sp.setPrefHeight(700.0);
		child1_sp.setPrefWidth(700.0);
		child1_sp_content_ap = new AnchorPane();	
		
		//Set child 1 content
		child1_sp.setContent(child1_sp_content_ap);
		
		//Set stage scene
		primaryStage.setScene(scene);
		
		//Set application title
		primaryStage.setTitle("Password Manager " + Version.GetVersion());
		
		//Add child 1 to root
		root.getChildren().addAll(child1_sp);
		
		//Set anchor pane as a content in child 1
		child1_sp_content_ap.setLayoutX(500);
		child1_sp_content_ap.setLayoutY(700);
		layout_y = 20.0;
		child1_sp_content_ap.setTopAnchor(child2_bt1, layout_y);

		//Add child 2
		child1_sp_content_ap.getChildren().add(child2_bt1);
		
		//Set button
		SetFirstButton();
		
		for(int index = 0; (RamDB.acc_db[index].set == true) && (index < 1000); index++) {
			configureAccButton(index);
		}					
	}
	
	public static void OpenAccount(ActionEvent event) throws Exception {
		Stage secondaryStage = new Stage();
		
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(MainController.class.getResource("Account.fxml").openStream());
		AccountController accountController = (AccountController)loader.getController();

		System.out.println("OpenAccount " + ((Node) event.getSource()).getId());
		
		accountController.GetAccount(Integer.parseInt(((Node) event.getSource()).getId()));					
	
		//To freeze parent window
		secondaryStage.initStyle(StageStyle.UTILITY);
		secondaryStage.initModality(Modality.WINDOW_MODAL);
		secondaryStage.initOwner(primaryStage);

		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(MainController.class.getResource("application.css").toExternalForm());
								
		secondaryStage.setScene(scene);
		secondaryStage.setTitle("Password Manager " + Version.GetVersion());
		secondaryStage.show();
		
	}
	
	
	public static void LogOut(ActionEvent event) throws Exception {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(MainController.class.getResource("Login.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(MainController.class.getResource("application.css").toExternalForm());
		primaryStage.setTitle("Password Manager " + Version.GetVersion());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
}
