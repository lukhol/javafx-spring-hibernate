package com.lukhol.politechnika.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.politechnika.Main;
import com.lukhol.politechnika.models.User;
import com.lukhol.politechnika.validators.UserValidator;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

@Component
public class RegisterController {

	@Autowired
	UserValidator userValidator;
	
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
			Main.changeScene(getClass().getResource("/fxml/LoginWindow.fxml"), "Login");

		};
		loginLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, onLoginLabelEventHandler);
	}
	
	@FXML
	public void onRegisterButtonClicked(){
		User user = new User();
		
		user.setUsername(usernameTextField.getText());
		user.setEmail(emailTextField.getText());
		
		System.out.println("Username: " + userValidator.validateUsername(user));
		System.out.println("Email: " + userValidator.validateEmail(user));
		//TO DO: do test to check it! moq java
	}
}
