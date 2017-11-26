package com.lukhol.politechnika.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.politechnika.Main;
import com.lukhol.politechnika.PageName;
import com.lukhol.politechnika.impl.ClientFactory;
import com.lukhol.politechnika.models.User;
import com.lukhol.politechnika.services.HelloService;
import com.lukhol.politechnika.services.UserService;

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
		new Thread(this::burlap).start();
		new Thread(this::hessian).start();
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
			Platform.runLater(() -> {
				errorMessageLabel.setManaged(false);
				errorMessageLabel.setVisible(false);
				loginGroup.setDisable(false);
			});
			
			Platform.exit();
			System.exit(0);
		}
	}
	
	public void hessian() {
		HelloService hessian = clientFactory.hessian(HelloService.class);
		User user = new User();
		user.setUsername("hessian");
		hessian.sayHello(user);
	}
	
	public void burlap() {
		HelloService burlap = clientFactory.burlap(HelloService.class);
		User user = new User();
		user.setUsername("burlap");
		burlap.sayHello(user);
	}
}
