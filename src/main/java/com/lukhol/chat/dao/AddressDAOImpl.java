package com.lukhol.chat.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lukhol.chat.models.Address;

@Repository
public class AddressDAOImpl implements AddressDAO{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	@SuppressWarnings("unchecked")
	public Address getAddress(String street, String postCode, String city) {
		List<Address> address = sessionFactory
				.getCurrentSession()
				.createQuery("from Address where street = ? and postCode = ? and city = ?")
				.setParameter(0, street)
				.setParameter(1, postCode)
				.setParameter(2, city)
				.list();
		
		if(!address.isEmpty())
			return address.get(0);
		
		return null;
	}

	@Override
	public Long addAddress(Address address) {
		return (Long)sessionFactory.getCurrentSession().save(address);
	}

	@Override
	public void addAddresses(List<Address> addresses) {
		throw new NotYetImplementedException();
	}
	
}
