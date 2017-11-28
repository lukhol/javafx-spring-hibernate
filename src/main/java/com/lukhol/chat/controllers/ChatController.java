package com.lukhol.chat.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.chat.fxcontrolls.UserListCell;
import com.lukhol.chat.impl.ClientFactory;
import com.lukhol.chat.models.User;
import com.lukhol.chat.services.ChatService;
import com.lukhol.chat.services.UserService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

@Component
public class ChatController {
	
	@Autowired
	ClientFactory clientFactory;
	
	@Autowired
	UserService userService;
	
	@FXML
	ListView<User> usersListView;
	
	@FXML
	ListView<User> conversationListView;
	
	@FXML
	Button hessianButton;
	
	@FXML
	Button burlapButton;
	
	@FXML
	void initialize() {
		
		List<User> allUsersFromDb = userService.getAllUsers();
		ObservableList<User> listViewItems = FXCollections.observableArrayList(allUsersFromDb);
		
		usersListView.setItems(listViewItems);
		
		usersListView.setCellFactory(new Callback<ListView<User>, 
            ListCell<User>>() {
                @Override 
                public ListCell<User> call(ListView<User> list) {
                    return new UserListCell();
                }
        	}
		);
		
		hessianButton.setOnAction(event -> {
			new Thread(this::hessian).start();
		});
		
		burlapButton.setOnAction(event -> {
			new Thread(this::burlap).start();
		});
	}
	
	private void hessian() {
		System.out.println("hessian started");
		ChatService hessian = clientFactory.hessian(ChatService.class);
		User user = new User();
		user.setUsername("hessianTest");
		System.out.println("Hessian adding: " + hessian.login(user));
	}
	
	private void burlap() {
		System.out.println("burlap started");
		ChatService burlap = clientFactory.burlap(ChatService.class);
		User user = new User();
		user.setUsername("burlapTest");
		System.out.println("Burlap adding: " + burlap.login(user));
	}
}
