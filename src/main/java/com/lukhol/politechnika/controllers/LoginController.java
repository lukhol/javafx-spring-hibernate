package com.lukhol.politechnika.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.politechnika.Main;
import com.lukhol.politechnika.PageName;
import com.lukhol.politechnika.models.User;
import com.lukhol.politechnika.services.UserService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Component
public class LoginController {

	@Autowired
	UserService userService;
	
	@FXML
	Label passwordLabel;
	
	@FXML
	Label usernameLabel;
	
	@FXML
	Button loginButton;
	
	@FXML
	TextField passwordTextField;
	
	@FXML
	PasswordField passwordField;
	
	@FXML
	TextField usernameTextField;
	
	@FXML
	Label registerLabel;
	
	@FXML
	void initialize() {

	}
	
	@FXML
	public void onLoginButtonClicked() {
		User user = new User();
		
		user.setUsername(usernameTextField.getText());
		user.setPassword(passwordField.getText());
		
		boolean isValid = userService.checkCredential(user);
		
		if(!isValid)
			return;
		
		Platform.exit();
		System.exit(0);
	}
	
	@FXML
	public void onRegisterClicked() {
		Main.changeScene(PageName.RegisterPage, "Register");
	}
	
}
