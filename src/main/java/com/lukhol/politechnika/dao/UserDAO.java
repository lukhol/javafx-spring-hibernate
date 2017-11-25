package com.lukhol.politechnika.dao;

import com.lukhol.politechnika.models.User;

public interface UserDAO {
	boolean addUser(User user);
	
	User getUserByUsername(String username);
}
