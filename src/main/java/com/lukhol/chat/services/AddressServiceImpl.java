package com.lukhol.chat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lukhol.chat.dao.AddressDAO;
import com.lukhol.chat.models.Address;

@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	AddressDAO addressDAO;
	
	@Override
	@Transactional
	public Address getAddress(String street, String postCode, String city) {
		return addressDAO.getAddress(street, postCode, city);
	}

	@Override
	@Transactional
	public void addAddress(Address address) {
		addressDAO.addAddress(address);
	}

	@Override
	@Transactional
	public void addAddresses(List<Address> addresses) {
		for(Address address : addresses) {
			if(addressDAO.getAddress(address.getStreet(), address.getPostCode(), address.getPostCode()) == null) {
				addressDAO.addAddress(address);
			}
		}
	}

}
