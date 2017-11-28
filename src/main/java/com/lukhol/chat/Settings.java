package com.lukhol.chat;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lukhol.chat.models.User;

@Component
@Scope("singleton")
public class Settings {
	
	private User loggedInUser;

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}
}
