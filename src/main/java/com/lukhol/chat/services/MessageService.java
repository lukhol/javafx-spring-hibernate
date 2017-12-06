package com.lukhol.chat.services;

import java.util.List;

import com.lukhol.chat.models.Message;

public interface MessageService {
	void addMessage(Message message);
	List<Message> getMessagesForConversation(String senderUsername, String receiverUsername);
	List<Message> getAllMessagesForConversation(String usernameOne, String usernameTwo);
	
	List<Message> getLastCountedMessagesForConversation(String usernameOne, String usernameTwo, int count);
}
