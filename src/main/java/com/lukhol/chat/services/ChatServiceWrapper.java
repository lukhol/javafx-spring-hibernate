package com.lukhol.chat.services;

import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.lukhol.chat.models.Conversation;
import com.lukhol.chat.models.Message;
import com.lukhol.chat.models.User;

public class ChatServiceWrapper implements ChatService {

	private XmlRpcClient client;
	private Gson gson;
	
	public ChatServiceWrapper(XmlRpcClient client) {
		this.client = client;
		this.gson = new Gson();
	}
	
	@Override
	public boolean login(User user) {
		String stringUser = gson.toJson(user);
		try {
			boolean result = (boolean)client.execute("ChatServiceWrapper.login", new Object[] { stringUser });
			return result;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void logout(User user) {
		String stringUser = gson.toJson(user);
		try {
			client.execute("ChatServiceWrapper.logout", new Object[] { stringUser });
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
		String stringSender = gson.toJson(sender);
		String stringReceiver = gson.toJson(receiver);
		String stringMessage = gson.toJson(message);
		try {
			boolean result = (boolean)client.execute("ChatServiceWrapper.sendMessage", new Object[] { stringSender, stringReceiver, stringMessage });
			return result;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Message> waitForMessages(User waiter) {
		String stringWaiter = gson.toJson(waiter);
		try {
			String stringMessagesList = (String)client.execute("ChatServiceWrapper.waitForMessages", new Object[] { stringWaiter});
			
			Type listType = new TypeToken<List<Message>>(){}.getType();
			List<Message> messages = new Gson().fromJson(stringMessagesList, listType);
			
			return messages;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getLoggedInUsers() {
		try {
			String loggedInUsersString = (String)client.execute("ChatServiceWrapper.getLoggedInUsers", new Object[] { "null" });
			
			Type listType = new TypeToken<List<String>>(){}.getType();
			List<String> loggedInUsers = new Gson().fromJson(loggedInUsersString, listType);
			
			return loggedInUsers;
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
