package com.lukhol.chat.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lukhol.chat.dao.AddressDAO;
import com.lukhol.chat.dao.UserDAO;
import com.lukhol.chat.models.Address;
import com.lukhol.chat.models.User;

@Service
public class UserServiceImpl implements UserService {
	
	private UserDAO userDAO;
	private AddressDAO addressDAO;
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserDAO userDAO, BCryptPasswordEncoder passwordEncoder, AddressDAO addressDAO) {
		if(userDAO == null)
			throw new NullPointerException(UserDAO.class.getName());
		
		if(passwordEncoder == null)
			throw new NullPointerException(BCryptPasswordEncoder.class.getName());
		
		if(addressDAO == null)
			throw new NullPointerException(AddressDAO.class.getName());
		
		this.userDAO = userDAO;
		this.passwordEncoder = passwordEncoder;
		this.addressDAO = addressDAO;
	}
	
	@Override
	@Transactional
	public boolean addUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		updateUserAddresesWithExistingInDatabase(user);
		setUserInEachAddressOfUserAddresses(user);
		
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

	@Override
	@Transactional
	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}
	
	private void updateUserAddresesWithExistingInDatabase(User user) {	
		List<Address> updatedUserAddressesWithDbInformation = new ArrayList<Address>();
		
		for(Address tempAddress : user.getAddress()) {
			Address addressFromDatabse = addressDAO.getAddress(tempAddress.getStreet(), tempAddress.getPostCode(), tempAddress.getCity());
			if(addressFromDatabse == null) {
				Long addressId = addressDAO.addAddress(tempAddress);
				tempAddress.setAddressId(addressId);
				updatedUserAddressesWithDbInformation.add(tempAddress);
			}
			else {
				updatedUserAddressesWithDbInformation.add(addressFromDatabse);
			}
		}
		
		user.setAddress(updatedUserAddressesWithDbInformation);
	}
	
	private void setUserInEachAddressOfUserAddresses(User user) {
		for(Address address : user.getAddress()) {
			List<User> habitancyList = address.getHabitancy();
			if(habitancyList == null) {
				habitancyList = new ArrayList<User>();
				address.setHabitancy(habitancyList);
			}
			
			habitancyList.add(user);
		}
	}
}
