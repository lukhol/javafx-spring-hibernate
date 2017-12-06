package com.lukhol.chat.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.chat.Main;
import com.lukhol.chat.PageName;
import com.lukhol.chat.models.Address;
import com.lukhol.chat.models.Pesel;
import com.lukhol.chat.models.User;
import com.lukhol.chat.services.UserService;
import com.lukhol.chat.validators.UserValidator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

@Component
public class RegisterController {

	@Autowired
	UserValidator userValidator;
	
	@Autowired
	UserService userService;
	
	@FXML
	private Label usernameLabel;
	
	@FXML
	private Label passwordLabel;
	
	@FXML
	private Label emailLabel;
	
	@FXML
	private TextField usernameTextField;
	
	@FXML
	private TextField emailTextField;
	
	@FXML
	private PasswordField passwordField;
	
	@FXML
	private TextField firstnameTextField;
	
	@FXML
	private TextField lastnameTextField;
	
	@FXML
	private TextField peselTextField;
	
	@FXML
	private Button addAddressButton;
	
	@FXML
	private ListView<Address> addressesListView;
	
	@FXML
	private Button registerButton;
	
	@FXML
	private Label loginLabel;
	
	@FXML
	void initialize() {
		EventHandler<MouseEvent> onLoginLabelEventHandler = event -> {
			Main.changeScene(PageName.LoginPage, "Login");
		};
		loginLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, onLoginLabelEventHandler);
		
		setAddAddressButtonAction();
		setAddressListViewOnClick();
		
	}
	
	private void setAddAddressButtonAction() {
		addAddressButton.setOnAction(event -> {
			Dialog<Address> addressDialog = new Dialog<Address>();
			addressDialog.setTitle("Adding address...");
			
			addressDialog.setHeaderText("Write address details below: ");
			addressDialog.setResizable(false);
			
			Label streetLabel = new Label("Street");
			Label postCodeLabel = new Label("Post code");
			Label cityLabel = new Label("City");
			
			TextField streetTextField = new TextField();
			TextField postCodeTextField = new TextField();
			TextField cityTextField = new TextField();
			
			GridPane gridPane = new GridPane();
			gridPane.add(streetLabel, 0, 0);
			gridPane.add(streetTextField, 1, 0);
			
			gridPane.add(postCodeLabel, 0, 1);
			gridPane.add(postCodeTextField, 1, 1);
			
			gridPane.add(cityLabel, 0, 2);
			gridPane.add(cityTextField, 1, 2);
			
			addressDialog.getDialogPane().setContent(gridPane);
			
			ButtonType buttonTypeOk = new ButtonType("Ok", ButtonData.OK_DONE);
			addressDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
			
			addressDialog.setResultConverter(new Callback<ButtonType, Address>(){

				@Override
				public Address call(ButtonType buttonType) {
					if(buttonType == buttonTypeOk) {
						Address address = new Address();
						address.setCity(cityTextField.getText());
						address.setPostCode(postCodeTextField.getText());
						address.setStreet(streetTextField.getText());
						return address;
					}
					
					return null;
				}
				
			});
			
			Optional<Address> optionalAddress = addressDialog.showAndWait();
			
			addressesListView.getItems().add(optionalAddress.get());
		});
	}
	
	private void setAddressListViewOnClick() {
		addressesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Address>() {
		    @Override
		    public void changed(ObservableValue<? extends Address> observable, Address oldValue, Address newValue) {
		        
		    	if(oldValue != null)
		    		addressesListView.getItems().remove(oldValue);
		    }
		});
	}
	
	@FXML
	public void onRegisterButtonClicked(){
		User user = new User();
		
		user.setUsername(usernameTextField.getText());
		user.setPassword(passwordField.getText());
		user.setEmail(emailTextField.getText());
		
		
		boolean usernameValidationResult = userValidator.validateUsername(user);
		boolean passwordValidationResult = userValidator.validatePassword(user);
		boolean emailValidationResult = userValidator.validateEmail(user);
		
		setStyle(usernameLabel, usernameValidationResult);
		setStyle(passwordLabel, passwordValidationResult);
		setStyle(emailLabel, emailValidationResult);
		
		boolean isValid = usernameValidationResult && passwordValidationResult && emailValidationResult;
		
		if(!isValid)
			return;
		
		Pesel pesel = new Pesel(Long.parseLong(peselTextField.getText()));
		pesel.setUser(user);
		user.setPesel(pesel);
		
		user.setFirstname(firstnameTextField.getText());
		user.setLastname(lastnameTextField.getText());
		
		boolean addingResult = userService.addUser(user);
		
		if(addingResult) {
			Main.changeScene(PageName.LoginPage, "Login");
		}		
	}
	
	private void setStyle(Node node, boolean isValid) {

		if(isValid)
			node.setStyle("-fx-text-fill: black;");
		else 
			node.setStyle("-fx-text-fill: red;");
	}
}
