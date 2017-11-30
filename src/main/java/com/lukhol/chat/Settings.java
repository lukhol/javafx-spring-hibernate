package com.lukhol.chat;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lukhol.chat.models.User;

@Component
@Scope("singleton")
public class Settings {
	
	private User loggedInUser;
	private List<Thread> threads = new ArrayList<Thread>();

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public List<Thread> getThreads() {
		return threads;
	}

	public void setThreads(List<Thread> threads) {
		this.threads = threads;
	}
}
