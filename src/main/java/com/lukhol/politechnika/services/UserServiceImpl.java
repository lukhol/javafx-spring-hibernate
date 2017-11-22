package com.lukhol.politechnika.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lukhol.politechnika.dao.UserDAO;
import com.lukhol.politechnika.models.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserDAO userDAO;
	
	@Override
	@Transactional
	public boolean addUser(User user) {
		return userDAO.addUser(user);
	}
}
