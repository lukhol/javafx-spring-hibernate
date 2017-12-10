package com.lukhol.chat.services;

import java.util.List;

import com.lukhol.chat.models.Address;

public interface AddressService {
	Address getAddress(String street, String postCode, String city);
	void addAddress(Address address);
	void addAddresses(List<Address> addresses);
}
