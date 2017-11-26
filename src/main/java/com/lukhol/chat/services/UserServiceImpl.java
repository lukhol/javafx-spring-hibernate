package com.lukhol.chat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lukhol.chat.dao.UserDAO;
import com.lukhol.chat.models.User;

@Service
public class UserServiceImpl implements UserService {
	
	private UserDAO userDAO;
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder) {
		if(userDAO == null)
			throw new NullPointerException(UserDAO.class.getName());
		
		if(passwordEncoder == null)
			throw new NullPointerException(BCryptPasswordEncoder.class.getName());
		
		this.userDAO = userDAO;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	@Transactional
	public boolean addUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userDAO.addUser(user);
	}
	
	@Override
	@Transactional
	public boolean checkIfUsernameExist(String username) {
		User user = userDAO.getUserByUsername(username);
		
		if(user == null)
			return false;
		
		return true;
	}

	@Override
	@Transactional
	public boolean checkIfEmailExist(String email) {
		User user = userDAO.getUserByEmail(email);
		
		if(user == null)
			return false;
		
		return true;
	}

	@Override
	@Transactional
	public boolean checkCredential(User user) {
		User userFromDb = userDAO.getUserByUsername(user.getUsername());
		
		if(userFromDb == null)
			return false;
		
		boolean isPasswordValid = passwordEncoder.matches(user.getPassword(), userFromDb.getPassword());
		
		if(!isPasswordValid)
			return false;
		
		return true;
	}
}
