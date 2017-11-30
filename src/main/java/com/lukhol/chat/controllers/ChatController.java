package com.lukhol.chat.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
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
	TabPane conversationsTabPane;
	
	@FXML
	void initialize() {
		createServices();
		setupUsersListView();
		
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
			String selectedUsername = selectedUser.getUsername();
			//Create tab if not exist.
			
			for(Tab existingTab : conversationsTabPane.getTabs()) {
				if(selectedUsername.equals(existingTab.getText())) {
					conversationsTabPane.getSelectionModel().select(existingTab);
					return;
				}
			}
			
			Tab tab = createTabForUser(selectedUsername);		
			conversationsTabPane.getTabs().add(tab);
		});
		
		conversationsTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
	        for(User userFromUsersListView : usersListView.getItems()) {
	        	if(newTab != null && userFromUsersListView.getUsername().equals(newTab.getText())) {
	        		selectedUser = userFromUsersListView;
	        		break;
	        	}
	        }
	    });
		
		conversationsTabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
	}
	
	private void waitForMessages() {
		while(true) {
			List<Message> listOfMessages = chatServiceToWaiting.waitForMessages(settings.getLoggedInUser());
			
			if(listOfMessages != null) {
				//listOfMessages.forEach(message -> testTextArea.appendText(message.getSender().getUsername() + ": " + message.getMessageContent() + "\n"));
				
				Platform.runLater(() -> {
					ObservableList<Tab> tabs = conversationsTabPane.getTabs();
					
					while(listOfMessages.size() > 0) {
						Message tempMessage = listOfMessages.get(listOfMessages.size() - 1);
						String senderUsername = tempMessage.getSender().getUsername();
						
						boolean foundUserTab = false;
						
						for(Tab userTab : tabs) {
							if(userTab.getText().equals(senderUsername)) {
								//Uzupe³nij userTab o wiadomoœæ
								VBox tabParentVBoxExisting = (VBox) userTab.getContent();
								ListView<String> messagesListViewFromTabExisting = (ListView<String>)tabParentVBoxExisting.getChildren().get(0);
								messagesListViewFromTabExisting.getItems().add(senderUsername + ": " + tempMessage.getMessageContent());
								foundUserTab = true;
								break;
							}
						}
						
						if(!foundUserTab) {
							//Je¿eli nie znaleziono taba to go utwórz i uzupe³nij.
							Tab tab = createTabForUser(senderUsername);
							VBox tabParentHBox = (VBox)tab.getContent();
							ListView<String> messagesListViewFromTab = (ListView<String>)tabParentHBox.getChildren().get(0);
							messagesListViewFromTab.getItems().add(senderUsername + ": " + tempMessage.getMessageContent());
							conversationsTabPane.getTabs().add(tab);
						}
						
						//Usuñ wiadomoœæ z listy dla obu przypadków:
						listOfMessages.remove(tempMessage);
					}
				});
			} else {
				break;
			}
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
					//Not working:
					ObservableList<User> usersListFromListView = usersListView.getItems();
					List<User> notAddedUsers = new ArrayList<User>();
					
					for(User userTemp : usersListFromListView) {
						List<User> notAddedUsersTemp = usersList.stream()
												 .filter(usr -> !usr.getUsername().equals(userTemp.getUsername()))
												 .collect(Collectors.toList());
						
						notAddedUsersTemp.forEach(u -> {
							if(!notAddedUsers.contains(u))
								notAddedUsers.add(u);
						});
					}
					
					notAddedUsers.forEach(user -> usersListFromListView.add(user));
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

	private Tab createTabForUser(String username) {
		Tab tab = new Tab(username);
		VBox vbox = new VBox();
		
		ListView<String> messagesAsStringListView = new ListView<String>();
		TextField messageTextField = new TextField();
		Button sendButtonInTab = new Button("Send");
		sendButtonInTab.setPrefWidth(500);
		
		messagesAsStringListView.setDisable(true);
		
		messageTextField.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode() == KeyCode.ENTER) {
				sendButtonInTab.fire();
			}
		});
		
		sendButtonInTab.setOnAction(event -> {
			Message message = new Message();
			message.setSender(settings.getLoggedInUser());
			message.setReceiver(selectedUser);
			message.setMessageContent(messageTextField.getText());
			
			VBox vboxFromTab = (VBox)sendButtonInTab.getParent();
			ListView<String> messagesListViewFromTab = (ListView<String>)vboxFromTab.getChildren().get(0);
			messagesListViewFromTab.getItems().add(settings.getLoggedInUser().getUsername() + ": " + messageTextField.getText());
			
			if(message == null || selectedUser == null) {
				System.out.println("selected user is null");
				return;
			}
			
			chatServiceToSending.sendMessage(settings.getLoggedInUser(), selectedUser, message);
			
			messageTextField.clear();
		});
		
		vbox.getChildren().addAll(messagesAsStringListView, messageTextField, sendButtonInTab);
		tab.setContent(vbox);
		
		return tab;
	}
}
