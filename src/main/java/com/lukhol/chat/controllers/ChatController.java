package com.lukhol.chat.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.chat.Settings;
import com.lukhol.chat.fxcontrolls.UserListCell;
import com.lukhol.chat.impl.ClientFactory;
import com.lukhol.chat.models.Message;
import com.lukhol.chat.models.User;
import com.lukhol.chat.services.ChatService;
import com.lukhol.chat.services.UserService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

@Component
public class ChatController {
	
	private final Logger logger = LoggerFactory.getLogger(ChatController.class);
	
	private ChatService chatServiceToSending;
	private ChatService chatServiceToWaiting;
	private ChatService chatServiceForLoggedUsers;
	
	private Object lock = new Object();
	private List<User> usersList = new ArrayList<>();
	
	private User selectedUser;
	
	@Autowired
	Settings settings;
	
	@Autowired
	ClientFactory clientFactory;
	
	@Autowired
	UserService userService;
	
	@FXML
	ListView<User> usersListView;
	
	@FXML
	ListView<User> conversationListView;
	
	@FXML
	Button sendButton;
	
	@FXML
	TextField messageTextField;
	
	@FXML
	TextArea testTextArea;
	
	@FXML
	void initialize() {
		createServices();
		setupUsersListView();
		setupSendingMessageEvents();
		
		new Thread(this::waitForMessages).start();
		new Thread(this::updateLoggedUsers).start();
	}
	
	private void createServices() {
		chatServiceForLoggedUsers = clientFactory.burlap(ChatService.class);
		chatServiceToSending = clientFactory.burlap(ChatService.class);
		chatServiceToWaiting = clientFactory.burlap(ChatService.class);
	}
	
	private void setupUsersListView() {
		List<String> allLoggedInUsers = chatServiceForLoggedUsers.getLoggedInUsers();
		allLoggedInUsers.remove(settings.getLoggedInUser().getUsername());
		
		usersList.clear();
		
		for(String tempUsername : allLoggedInUsers) {
			User tempUser = new User();
			tempUser.setUsername(tempUsername);
			usersList.add(tempUser);
		}
		
		ObservableList<User> listViewItems = FXCollections.observableArrayList(usersList);
		
		usersListView.setItems(listViewItems);
		
		usersListView.setCellFactory(new Callback<ListView<User>, 
            ListCell<User>>() {
                @Override 
                public ListCell<User> call(ListView<User> list) {
                    return new UserListCell();
                }
        	}
		);
		
		usersListView.setOnMouseClicked(e -> {
			selectedUser = usersListView.getSelectionModel().getSelectedItem();
		});
	}
	
	private void setupSendingMessageEvents() {
		messageTextField.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER) {
				sendButton.fire();
			}
		});
		
		sendButton.setOnAction(e -> {
			Message message = new Message();
			message.setSender(settings.getLoggedInUser());
			message.setReceiver(selectedUser);
			message.setMessageContent(messageTextField.getText());
			testTextArea.appendText(settings.getLoggedInUser().getUsername() + ": " + messageTextField.getText() + "\n");
			
			if(message == null || selectedUser == null) {
				System.out.println("selected user is null");
				return;
			}
			
			chatServiceToSending.sendMessage(settings.getLoggedInUser(), selectedUser, message);
			
			Platform.runLater(() -> {
				messageTextField.setText("");
			});
		});
	}
	
	private void waitForMessages() {
		while(true) {
			List<Message> listOfMessages = chatServiceToWaiting.waitForMessages(settings.getLoggedInUser());
			
			if(listOfMessages != null)
				listOfMessages.forEach(message -> testTextArea.appendText(message.getSender().getUsername() + ": " + message.getMessageContent() + "\n"));
		}
	}
	
	private void updateLoggedUsers() {
		while(true) {
			synchronized(lock) {
				List<String> allLoggedInUsers = chatServiceForLoggedUsers.getLoggedInUsers();
				allLoggedInUsers.remove(settings.getLoggedInUser().getUsername());
				
				usersList.clear();
				
				for(String tempUsername : allLoggedInUsers) {
					User tempUser = new User();
					tempUser.setUsername(tempUsername);
					usersList.add(tempUser);
				}
				
				Platform.runLater(() -> {
					ObservableList<User> listViewItems = FXCollections.observableArrayList(usersList);
					usersListView.setItems(listViewItems);
				});
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.info("Refreshin logged in users throw exception during Thread.Sleep() method.");
				e.printStackTrace();
			}
		}
	}
}
