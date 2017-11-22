package com.lukhol.politechnika.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.politechnika.Main;
import com.lukhol.politechnika.dao.UserDAO;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Component
public class LoginController {

	@Autowired
	UserDAO userDAO;
	
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
		System.out.println(userDAO.toString());
	}
	
	@FXML
	public void onRegisterClicked() {
		Main.changeScene(getClass().getResource("/fxml/RegistrationWindow.fxml"), "Register");
	}
	
}
