package com.lukhol.chat.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lukhol.chat.models.Message;
import com.lukhol.chat.models.User;

@Repository
public class MessageDAOImpl implements MessageDAO {

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public void add(Message message) {
		sessionFactory.getCurrentSession().save(message);
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<Message> getMessagesForConversation(User sender, User receiver) {
		return (List<Message>)sessionFactory.getCurrentSession().createQuery("from Message where sender = ? and receiver = ?")
			.setParameter(0,  sender)
			.setParameter(1,  receiver)
			.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Message> getLastCountedMessagesForConversation(User sender, User receiver, int count) {
		return (List<Message>)sessionFactory.getCurrentSession()
				.createQuery("from Message where sender = ? and receiver = ? order by messageId desc")
				.setParameter(0, sender)
				.setParameter(1, receiver)
				.setMaxResults(count)
				.list();
	}
}
