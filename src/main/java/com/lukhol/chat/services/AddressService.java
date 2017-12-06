package com.lukhol.chat.services;

import com.lukhol.chat.models.Address;

public interface AddressService {
	Address getAddress(String street, String postCode, String city);
	void addAddress(Address address);
}
