package com.lukhol.chat.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lukhol.chat.models.User;

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
	public User getUserByUsername(String username) {
		return getUserByQueryAndOneParam(username, "from User where username=?");
	}

	@Override
	public User getUserByEmail(String email) {
		return getUserByQueryAndOneParam(email, "from User where email=?");
	}
	
	@SuppressWarnings("unchecked")
	private User getUserByQueryAndOneParam(Object param, String query){
		List<User> usersList = sessionFactory.getCurrentSession().createQuery(query).setParameter(0, param).list();

		if(usersList == null || usersList.isEmpty())
			return null;
		
		return usersList.get(0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {
		return sessionFactory.getCurrentSession().createQuery("from User order by userId").list();
	}
}
