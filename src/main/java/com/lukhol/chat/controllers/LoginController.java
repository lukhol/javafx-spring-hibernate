package com.lukhol.chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.chat.Main;
import com.lukhol.chat.PageName;
import com.lukhol.chat.Settings;
import com.lukhol.chat.impl.ClientFactory;
import com.lukhol.chat.models.User;
import com.lukhol.chat.services.ChatService;
import com.lukhol.chat.services.UserService;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Component
public class LoginController {

	@Autowired
	Settings settings;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ClientFactory clientFactory;
	
	@FXML
	Label passwordLabel;
	
	@FXML
	Label usernameLabel;
	
	@FXML
	Button loginButton;
	
	@FXML
	PasswordField passwordField;
	
	@FXML
	TextField usernameTextField;
	
	@FXML
	Label registerLabel;
	
	@FXML 
	Label errorMessageLabel;
	
	@FXML
	Group loginGroup;
	
	@FXML
	void initialize() {
		loginButton.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER) {
				loginButton.fire();
			}
		}); 
		
		errorMessageLabel.setVisible(false);
		errorMessageLabel.setManaged(false);
	}
	
	@FXML
	public void onLoginButtonClicked() {
		loginGroup.setDisable(true);
		new Thread(this::login).start();
	}
	
	@FXML
	public void onRegisterClicked() {
		Main.changeScene(PageName.RegisterPage, "Register");
	}
	
	public void login() {
		User user = new User();
		
		user.setUsername(usernameTextField.getText());
		user.setPassword(passwordField.getText());
		
		boolean isValid = userService.checkCredential(user);
		
		if(!isValid) {
			Platform.runLater(() -> {
				errorMessageLabel.setManaged(true);
				errorMessageLabel.setVisible(true);
				loginGroup.setDisable(false);
			});
			return;
		} else {
			
			ChatService chatService = clientFactory.burlap(ChatService.class);
			if(!chatService.login(user))
				return;
			
			settings.setLoggedInUser(user);
			
			Platform.runLater(() -> {
				errorMessageLabel.setManaged(false);
				errorMessageLabel.setVisible(false);
				loginGroup.setDisable(false);
				Main.changeScene(PageName.ChatPage, "Chat (logged as " + user.getUsername() + ")");
			});
		}
	}
}
