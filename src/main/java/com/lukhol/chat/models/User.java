package com.lukhol.chat.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class User implements Serializable{
	
	@Transient
	private static final long serialVersionUID = 804447377720668334L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long userId;
	
	@Column(nullable=false, unique=true)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	@OneToMany(mappedBy="sender")
	private List<Message> sendedMessage;
	
	@OneToMany(mappedBy="receiver")
	private List<Message> receivedMessage;
	
	private String firstname;
	
	private String lastname;
	
	@ManyToMany(mappedBy="habitancy")
	private List<Address> address;
	
	@OneToOne(mappedBy="user")
	private Pesel pesel;
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public List<Message> getSendedMessage() {
		return sendedMessage;
	}

	public void setSendedMessage(List<Message> sendedMessage) {
		this.sendedMessage = sendedMessage;
	}

	public List<Message> getReceivedMessage() {
		return receivedMessage;
	}

	public void setReceivedMessage(List<Message> receivedMessage) {
		this.receivedMessage = receivedMessage;
	}

	public Pesel getPesel() {
		return pesel;
	}

	public void setPesel(Pesel pesel) {
		this.pesel = pesel;
	}

	public List<Address> getAddress() {
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	@Override
	public boolean equals(Object otherObject) {
		
		if(this == otherObject)
			return true;
		
		if(otherObject == null)
			return false;
		
		if(getClass() != otherObject.getClass())
			return false;
		
		User otherUser = (User)otherObject;
		
		return this.getUsername().equals(otherUser.getUsername());
	}
	
	@Override
	public String toString() {
		return this.getUsername();
	}
}
