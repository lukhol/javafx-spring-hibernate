package com.lukhol.chat.controllers;

import java.util.List;
import java.util.Optional;

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
	
	private ChatService chatServiceToSending;
	private ChatService chatServiceToWaiting;
	
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
		
		List<User> allUsersFromDb = userService.getAllUsers();
		String loggedUserUsername = settings.getLoggedInUser().getUsername();

		Optional<User> findedUser = allUsersFromDb.stream()
			.filter(user -> user.getUsername().equals(loggedUserUsername))
			.findFirst();
		
		allUsersFromDb.remove(findedUser.get());
		
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
		
		messageTextField.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER) {
				sendButton.fire();
			}
		});
		
		chatServiceToSending = clientFactory.burlap(ChatService.class);
		
		sendButton.setOnAction(e -> {
			User selectedUser = usersListView.getSelectionModel().getSelectedItem();
			Message message = new Message();
			message.setSender(settings.getLoggedInUser());
			message.setReceiver(selectedUser);
			message.setMessageContent(messageTextField.getText());
			testTextArea.appendText(settings.getLoggedInUser().getUsername() + ": " + messageTextField.getText() + "\n");
			chatServiceToSending.sendMessage(settings.getLoggedInUser(), selectedUser, message);
			
			Platform.runLater(() -> {
				messageTextField.setText("");
			});
		});
		
		new Thread(this::waitForMessages).start();
	}
	
	private void waitForMessages() {
		chatServiceToWaiting = clientFactory.burlap(ChatService.class);
		
		while(true) {
			List<Message> listOfMessages = chatServiceToWaiting.waitForMessages(settings.getLoggedInUser());
			listOfMessages.forEach(message -> testTextArea.appendText(message.getSender().getUsername() + ": " + message.getMessageContent() + "\n"));
			System.out.println("Message from: " + listOfMessages.get(0).getSender().getUsername() + ", to: " + settings.getLoggedInUser().getUsername() + ", message: " + listOfMessages.get(0).getMessageContent());
		}
	}
}
