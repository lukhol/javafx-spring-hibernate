package com.lukhol.chat.dao;

import java.util.List;

import com.lukhol.chat.models.Message;
import com.lukhol.chat.models.User;

public interface MessageDAO {
	void add(Message message);
	List<Message> getMessagesForConversation(User sender, User receiver);
	List<Message> getLastCountedMessagesForConversation(User sender, User receiver, int count);
}
