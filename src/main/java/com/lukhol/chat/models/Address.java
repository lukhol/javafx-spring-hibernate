package com.lukhol.chat.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long addressId;
	
	private String street;
	
	private String postCode;
	
	private String city;
	
	@ManyToMany
	private List<User> habitancy;
	
	public List<User> getHabitancy() {
		return habitancy;
	}

	public void setHabitancy(List<User> habitancy) {
		this.habitancy = habitancy;
	}

	public Long getAddressId() {
		return addressId;
	}
	
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getPostCode() {
		return postCode;
	}
	
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	@Override
	public String toString() {
		return city + ", " + street + ", " + postCode + ".";
	}
}
