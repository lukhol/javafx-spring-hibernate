package com.lukhol.politechnika.controllers;

import org.springframework.stereotype.Component;

import com.lukhol.politechnika.Main;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

@Component
public class RegisterController {

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
}
