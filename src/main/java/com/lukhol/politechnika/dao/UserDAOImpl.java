package com.lukhol.politechnika.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lukhol.politechnika.models.User;

@Repository
public class UserDAOImpl implements UserDAO{

	private static final Logger logger = Logger.getLogger(UserDAOImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public boolean addUser(User user) {	
		try {
			sessionFactory.getCurrentSession().save(user);
			logger.info("Added user to the database - " + user.toString());
			return true;
		} catch (Exception e) {
			logger.info("Adding user to the database faild." + e.getStackTrace().toString());
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public User getUserByUsername(String username) {
		List<User> usersList = sessionFactory.getCurrentSession().createQuery("from User where username=?").setParameter(0, username).list();

		if(usersList == null || usersList.isEmpty())
			return null;
		
		return usersList.get(0);
	}
}
