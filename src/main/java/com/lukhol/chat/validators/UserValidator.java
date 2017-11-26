package com.lukhol.chat.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lukhol.chat.models.User;
import com.lukhol.chat.services.UserService;

@Component
public class UserValidator {
	
	@Autowired
	UserService userService;
	
	private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	
	public boolean validateUsername(User user) {
		String username = user.getUsername();
		
		if(username == null ||username.length() <= 5)
			return false;
		
		if(userService.checkIfUsernameExist(username))
			return false;
		
		return true;
	}
	
	public boolean validateEmail(User user) {
		String email = user.getEmail();
		
		if(email == null || email.length() <= 5)
			return false;
		
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		
		if(!matcher.matches())
			return false;
		
		if(userService.checkIfEmailExist(email))
			return false;
		
		return true;
	}
	
	public boolean validatePassword(User user) {
		String password = user.getPassword();

		if(password == null || password.length() <= 5)
			return false;
		
		return true;
	}
}
