package com.lukhol.chat.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.chat.Settings;
import com.lukhol.chat.fxcontrolls.MessageListCell;
import com.lukhol.chat.fxcontrolls.UserListCell;
import com.lukhol.chat.impl.MyClientFactory;
import com.lukhol.chat.models.Message;
import com.lukhol.chat.models.User;
import com.lukhol.chat.models.fx.MessageFX;
import com.lukhol.chat.services.ChatService;
import com.lukhol.chat.services.MessageService;
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

	private ChatService chatService;

	private List<User> usersList = new ArrayList<>();

	private User selectedUser;

	@Autowired
	Settings settings;

	@Autowired
	MyClientFactory clientFactory;

	@Autowired
	UserService userService;
	
	@Autowired
	MessageService messageService;

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
		setupConversationsTabPane();
		setupThreads();
	}

	private void createServices() {
		chatService = clientFactory.createServiceImplementation(ChatService.class);
	}

	private void setupUsersListView() {
		usersListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
			@Override
			public ListCell<User> call(ListView<User> list) {
				return new UserListCell();
			}
		});
		
		resolveLoggedInUsersInformation();
		setupLoggedInUsersListOnClick();
	}
	
	private void resolveLoggedInUsersInformation() {
		List<String> allLoggedInUsers = chatService.getLoggedInUsers();
		allLoggedInUsers.remove(settings.getLoggedInUser().getUsername());

		usersList.clear();

		for (String tempUsername : allLoggedInUsers) {
			User tempUser = new User();
			tempUser.setUsername(tempUsername);
			usersList.add(tempUser);
		}

		ObservableList<User> listViewItems = FXCollections.observableArrayList(usersList);
		usersListView.setItems(listViewItems);
	}
	
	private void setupLoggedInUsersListOnClick() {
		usersListView.setOnMouseClicked(e -> {
			selectedUser = usersListView.getSelectionModel().getSelectedItem();
			String selectedUsername = selectedUser.getUsername();
			
			// Create tab if not exist.
			for (Tab existingTab : conversationsTabPane.getTabs()) {
				if (selectedUsername.equals(existingTab.getText())) {
					conversationsTabPane.getSelectionModel().select(existingTab);
					return;
				}
			}

			Tab tab = createTabForUser(selectedUsername);
			conversationsTabPane.getTabs().add(tab);
			conversationsTabPane.getSelectionModel().select(tab);
		});
	}
	
	private void setupConversationsTabPane() {
		conversationsTabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			for (User userFromUsersListView : usersListView.getItems()) {
				if (newTab != null && userFromUsersListView.getUsername().equals(newTab.getText())) {
					selectedUser = userFromUsersListView;
					newTab.getStyleClass().remove("message-on-tab");
					break;
				}
			}
		});

		conversationsTabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
	}

	private void setupThreads() {
		settings.getExecutorService().submit(new Thread(this::waitForMessagesAsync));
		settings.getExecutorService().submit(new Thread(this::updateLoggedUsersAsync));
	}
	
	private void waitForMessagesAsync() {
		while (true) {
			List<Message> listOfMessages = chatService.waitForMessages(settings.getLoggedInUser());

			if (listOfMessages != null) {

				Platform.runLater(() -> {
					ObservableList<Tab> tabs = conversationsTabPane.getTabs();

					while (listOfMessages.size() > 0) {
						Message tempMessage = listOfMessages.get(listOfMessages.size() - 1);
						String senderUsername = tempMessage.getSender().getUsername();

						boolean foundUserTab = false;

						for (Tab userTab : tabs) {
							if (userTab.getText().equals(senderUsername)) {
								// Uzupe³nij userTab o wiadomoœæ
								addMessageToExistingTab(userTab, senderUsername, tempMessage);
								foundUserTab = true;
								break;
							}
						}

						if (!foundUserTab) {
							// Je¿eli nie znaleziono taba to go utwórz i uzupe³nij.
							Tab tab = createTabForUser(senderUsername);
							addMessageToExistingTab(tab, senderUsername, tempMessage);
							conversationsTabPane.getTabs().add(tab);
						}

						// Usuñ wiadomoœæ z listy dla obu przypadków:
						listOfMessages.remove(tempMessage);
						
						tempMessage.setTimestamp(new Date());
						//Dodawanie wiadomisci do bazdy danych po stronie clienta
						messageService.addMessage(tempMessage);
						
						printMessages(senderUsername, settings.getLoggedInUser().getUsername());
					}
				});
			} else {
				break;
			}
		}
	}
	
	private void printMessages(String sender, String receiver) {
		List<Message> messagesOne = messageService.getLastCountedMessagesForConversation(sender,  receiver, 5);
		
		messagesOne.forEach(msg -> System.out.println("From: " + msg.getSender().getUsername() + 
				", To: " + msg.getReceiver() .getUsername()+ ": " + msg.getMessageContent()));
	}
	
	@SuppressWarnings("unchecked")
	private void addMessageToExistingTab(Tab tab, String username, Message message) {
		VBox tabParentVBoxExisting = (VBox) tab.getContent();
		ListView<MessageFX> messagesListViewFromTabExisting = (ListView<MessageFX>) tabParentVBoxExisting.getChildren().get(0);
		MessageFX messageFX = new MessageFX();
		messageFX.setMessage(message);
		messageFX.setClientOwner(false);
		messagesListViewFromTabExisting.getItems().add(messageFX);
	}
	
	private void updateLoggedUsersAsync() {
		while (true && !Thread.currentThread().isInterrupted()) {
			List<String> allLoggedInUsers = chatService.getLoggedInUsers();
			allLoggedInUsers.remove(settings.getLoggedInUser().getUsername());

			usersList.clear();

			for (String tempUsername : allLoggedInUsers) {
				User tempUser = new User();
				tempUser.setUsername(tempUsername);
				usersList.add(tempUser);
			}
			
			Platform.runLater(() -> {
				// Update list
				ObservableList<User> usersListFromListView = usersListView.getItems();
				for (User usr : usersList) {
					if (!usersListFromListView.contains(usr)) {
						usersListFromListView.add(usr);
					}
				}

				List<User> tempUsersList = new ArrayList<User>();
				for (User usr2 : usersListFromListView) {
					if (!usersList.contains(usr2))
						tempUsersList.add(usr2);
				}

				usersListFromListView.removeAll(tempUsersList);
				
				usersListView.setItems(null);
				usersListView.setItems(usersListFromListView);
			});

			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				logger.info("Refreshin logged in users throw exception during Thread.Sleep() method.");
				return;
			}
		}
	}

	//UI Methods:
	private Tab createTabForUser(String username) {
		Tab tab = new Tab(username);
		VBox vbox = new VBox();

		ListView<MessageFX> messagesListView = new ListView<MessageFX>();
		messagesListView.setPrefHeight(2000);
		TextField messageTextField = new TextField();
		Button sendButtonInTab = new Button("Send");
		sendButtonInTab.getStyleClass().add("send-button");
		sendButtonInTab.setPrefWidth(1500);
		
		messagesListView.setCellFactory(new Callback<ListView<MessageFX>, ListCell<MessageFX>>() {
			@Override
			public ListCell<MessageFX> call(ListView<MessageFX> list) {
				return new MessageListCell(messagesListView);
			}
		});

		vbox.getChildren().addAll(messagesListView, messageTextField, sendButtonInTab);
		tab.setContent(vbox);

		messageTextField.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ENTER) {
				sendButtonInTab.fire();
			}
		});

		sendButtonInTab.setOnAction(event -> {
			Message message = new Message();
			message.setSender(settings.getLoggedInUser());
			message.setReceiver(selectedUser);
			message.setMessageContent(messageTextField.getText());

			VBox vboxFromTab = (VBox) sendButtonInTab.getParent();
			@SuppressWarnings("unchecked")
			ListView<MessageFX> messagesListViewFromTab = (ListView<MessageFX>) vboxFromTab.getChildren().get(0);
			MessageFX ownerMessage = new MessageFX();
			ownerMessage.setMessage(message);
			ownerMessage.setClientOwner(true);
			messagesListViewFromTab.getItems().add(ownerMessage);

			if (message == null || selectedUser == null) {
				System.out.println("selected user is null");
				return;
			}

			chatService.sendMessage(settings.getLoggedInUser(), selectedUser, message);

			messageTextField.clear();
		});
		
		return tab;
	}

}
