package com.lukhol.politechnika.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lukhol.politechnika.models.User;

@Repository
public class UserDAOImpl implements UserDAO{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public void addUser(User user) {	
		sessionFactory.getCurrentSession().save(user);
	}
}
