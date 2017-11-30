package com.lukhol.chat.services;

import java.util.List;

import com.lukhol.chat.models.Conversation;
import com.lukhol.chat.models.Message;
import com.lukhol.chat.models.User;

public interface ChatService {
	boolean login(User user);
	void logout(User user);
	
	Conversation createConversation(User sender, User receiver);
	boolean sendMessage(User sender, User receiver, Message message);
	
	List<Message> waitForMessages(User waiter);
	
	List<String> getLoggedInUsers();
}
