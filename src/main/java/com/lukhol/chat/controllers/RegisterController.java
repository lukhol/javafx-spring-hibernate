package com.lukhol.chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.chat.Main;
import com.lukhol.chat.PageName;
import com.lukhol.chat.models.User;
import com.lukhol.chat.services.UserService;
import com.lukhol.chat.validators.UserValidator;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
	private Button registerButton;
	
	@FXML
	private Label loginLabel;
	
	@FXML
	void initialize() {
		EventHandler<MouseEvent> onLoginLabelEventHandler = event -> {
			Main.changeScene(PageName.LoginPage, "Login");
		};
		loginLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, onLoginLabelEventHandler);
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
		
		if(userService.addUser(user)) {
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
