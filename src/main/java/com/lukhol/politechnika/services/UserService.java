package com.lukhol.politechnika.services;

import com.lukhol.politechnika.models.User;

public interface UserService {
	boolean addUser(User user);
	
	boolean checkIfUsernameExist(String username);
	boolean checkIfEmailExist(String email);
	boolean checkCredential(User user);
}
