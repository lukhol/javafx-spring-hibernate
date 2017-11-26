package com.lukhol.chat.dao;

import com.lukhol.chat.models.User;

public interface UserDAO {
	boolean addUser(User user);
	
	User getUserByUsername(String username);
	User getUserByEmail(String email);
}
