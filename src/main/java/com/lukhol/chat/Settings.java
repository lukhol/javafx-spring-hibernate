package com.lukhol.chat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.lukhol.chat.models.User;

@Component
@Scope("singleton")
public class Settings {
	
	private User loggedInUser;
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private Protocol protocol;

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public Protocol getProtocol() {
		return protocol;
	}

	public void setProtocol(Protocol protocol) {
		this.protocol = protocol;
	}

	public ExecutorService getExecutorService() {
		return executorService;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
}
