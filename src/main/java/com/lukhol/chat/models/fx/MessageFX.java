package com.lukhol.chat.models.fx;

import com.lukhol.chat.models.Message;

public class MessageFX {
	private Message message;
	private boolean clientOwner;
	
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public boolean isClientOwner() {
		return clientOwner;
	}
	public void setClientOwner(boolean clientOwner) {
		this.clientOwner = clientOwner;
	}
}
