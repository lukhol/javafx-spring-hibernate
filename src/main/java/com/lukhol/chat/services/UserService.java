package com.lukhol.chat.services;

import java.util.List;

import com.lukhol.chat.models.User;

public interface UserService {
	boolean addUser(User user);
	
	boolean checkIfUsernameExist(String username);
	boolean checkIfEmailExist(String email);
	boolean checkCredential(User user);
	
	List<User> getAllUsers();
}
