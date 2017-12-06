package com.lukhol.chat.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Pesel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long peselId;
	
	@Column(unique = true, nullable = false)
	private Long pesel;
	
	@OneToOne
	private User user;

	public Pesel() {
		
	}
	
	public Pesel(Long pesel) {
		this.pesel = pesel;
	}
	
	public Long getPeselId() {
		return peselId;
	}

	public void setPeselId(Long peselId) {
		this.peselId = peselId;
	}

	public Long getPesel() {
		return pesel;
	}

	public void setPesel(Long pesel) {
		this.pesel = pesel;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
