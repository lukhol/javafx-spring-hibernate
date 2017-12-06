package com.lukhol.chat.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lukhol.chat.dao.MessageDAO;
import com.lukhol.chat.dao.UserDAO;
import com.lukhol.chat.models.Message;
import com.lukhol.chat.models.User;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	MessageDAO messageDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Override
	@Transactional
	public void addMessage(Message message) {
		User sender = userDAO.getUserByUsername(message.getSender().getUsername());
		User receiver = userDAO.getUserByUsername(message.getReceiver().getUsername());
		
		message.setSender(sender);
		message.setReceiver(receiver);
		message.setDelivered(true);
		
		messageDAO.add(message);
	}

	@Override
	@Transactional
	public List<Message> getMessagesForConversation(String senderUsername, String receiverUsername) {
		User sender = userDAO.getUserByUsername(senderUsername);
		User receiver = userDAO.getUserByUsername(receiverUsername);
		
		return messageDAO.getMessagesForConversation(sender, receiver);
	}

	@Override
	@Transactional
	public List<Message> getAllMessagesForConversation(String usernameOne, String usernameTwo) {
		User userOne = userDAO.getUserByUsername(usernameOne);
		User userTwo = userDAO.getUserByUsername(usernameTwo);
		
		List<Message> messagesOne = messageDAO.getMessagesForConversation(userOne, userTwo);
		List<Message> messagesTwo = messageDAO.getMessagesForConversation(userTwo, userOne);
		
		messagesOne.addAll(messagesTwo);
		
		Collections.sort(messagesOne, new Comparator<Message>() {
			  public int compare(Message o1, Message o2) {
			      return o1.getTimestamp().compareTo(o2.getTimestamp());
			  }
		});
		
		return messagesOne;
	}

	@Override
	@Transactional
	public List<Message> getLastCountedMessagesForConversation(String usernameOne, String usernameTwo, int count) {
		User userOne = userDAO.getUserByUsername(usernameOne);
		User userTwo = userDAO.getUserByUsername(usernameTwo);
		
		List<Message> messagesOne = messageDAO.getLastCountedMessagesForConversation(userOne, userTwo, count);
		List<Message> messagesTwo = messageDAO.getLastCountedMessagesForConversation(userTwo, userOne, count);
		
		messagesOne.addAll(messagesTwo);
		
		Collections.sort(messagesOne, new Comparator<Message>() {
			  public int compare(Message o1, Message o2) {
			      return o1.getTimestamp().compareTo(o2.getTimestamp());
			  }
		});
		
		int messagesListSize = messagesOne.size();
		
		return messagesOne.subList(messagesListSize - count, messagesListSize);
	}
}
