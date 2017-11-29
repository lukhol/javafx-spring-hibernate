package com.lukhol.chat.models;

import java.io.Serializable;
import java.util.List;

public class Conversation implements Serializable{
	
	private static final long serialVersionUID = -8768479341445929890L;
	
	private User userOne;
	private User userTwo;
	private List<Message> listOfMessages;
	private boolean isUpToDate;
	
	public User getUserOne() {
		return userOne;
	}
	
	public void setUserOne(User userOne) {
		this.userOne = userOne;
	}
	
	public User getUserTwo() {
		return userTwo;
	}
	
	public void setUserTwo(User userTwo) {
		this.userTwo = userTwo;
	}
	
	public List<Message> getListOfMessages() {
		return listOfMessages;
	}
	
	public void setListOfMessages(List<Message> listOfMessages) {
		this.listOfMessages = listOfMessages;
	}
	
	public boolean isUpToDate() {
		return isUpToDate;
	}
	
	public void setUpToDate(boolean isUpToDate) {
		this.isUpToDate = isUpToDate;
	}
}
