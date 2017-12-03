package com.lukhol.chat.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import com.lukhol.chat.models.Conversation;
import com.lukhol.chat.models.Message;
import com.lukhol.chat.models.User;

public class ChatServiceWrapper implements ChatService {

	private XmlRpcClient client;
	
	public ChatServiceWrapper(XmlRpcClient client) {
		this.client = client;
	}
	
	@Override
	public boolean login(User user) {
		try {
			boolean result = (boolean)client.execute("ChatService.login", new Object[] { user });
			return result;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void logout(User user) {
		try {
			client.execute("ChatService.logout", new Object[] { user });
		} catch (XmlRpcException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Conversation createConversation(User sender, User receiver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean sendMessage(User sender, User receiver, Message message) {
		try {
			boolean result = (boolean)client.execute("ChatService.sendMessage", new Object[] { sender, receiver, message });
			return result;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Message> waitForMessages(User waiter) {
		try {
			Object[] messages = (Object[])client.execute("ChatService.waitForMessages", new Object[] { waiter});
			
			ArrayList<Message> messagesList = new ArrayList<>();
		
			if(messages == null)
				return messagesList;
			
			for(Object obj : messages) {
				messagesList.add((Message)obj);
			}
			
			return messagesList;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getLoggedInUsers() {
		try {
			Object[] loggedInUsers = (Object[])client.execute("ChatService.getLoggedInUsers", new Object[0]);
			
			ArrayList<String> listOfUsers = new ArrayList<>();
			for(Object obj : loggedInUsers) {
				listOfUsers.add((String)obj);
			}
			
			return listOfUsers;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
