package com.lukhol.chat.models;

import java.io.Serializable;
import java.security.Timestamp;

public class Message implements Serializable{
	
	private static final long serialVersionUID = 3376963405912668895L;
	
	private Timestamp timestamp;
	private boolean delivered;
	private User sender;
	private User receiver;
	private String messageContent;
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isDelivered() {
		return delivered;
	}
	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
}
