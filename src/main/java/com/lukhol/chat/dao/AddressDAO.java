package com.lukhol.chat.dao;

import com.lukhol.chat.models.Address;

public interface AddressDAO {
	Address getAddress(String street, String postCode, String city);
	void addAddress(Address address);
}
