package com.lukhol.chat.services;

import com.lukhol.chat.models.User;

public interface ChatService {
	boolean login(User user);
	boolean logout(User user);
}
