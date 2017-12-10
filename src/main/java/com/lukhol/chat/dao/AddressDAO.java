package com.lukhol.chat.dao;

import java.util.List;

import com.lukhol.chat.models.Address;

public interface AddressDAO {
	Address getAddress(String street, String postCode, String city);
	Long addAddress(Address address);
	void addAddresses(List<Address> addresses);
}
