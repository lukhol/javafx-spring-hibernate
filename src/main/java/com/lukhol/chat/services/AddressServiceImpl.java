package com.lukhol.chat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lukhol.chat.dao.AddressDAO;
import com.lukhol.chat.models.Address;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	AddressDAO addressDAO;
	
	@Override
	public Address getAddress(String street, String postCode, String city) {
		return addressDAO.getAddress(street, postCode, city);
	}

	@Override
	public void addAddress(Address address) {
		addressDAO.addAddress(address);
	}

}
