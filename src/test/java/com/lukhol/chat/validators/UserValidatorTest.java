package com.lukhol.chat.validators;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.lukhol.chat.models.User;
import com.lukhol.chat.services.UserService;
import com.lukhol.chat.validators.UserValidator;

public class UserValidatorTest {

	@Mock
	private UserService userService;
	
	@InjectMocks
	private static UserValidator userValidator;
	
	private User user;
	private boolean usernameValidationResult;
	
	@BeforeClass
	public static void initialize() {
		userValidator = new UserValidator();
	}
	
	@Before
	public void beforEachInit() {
		user = new User();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void wrongUsername_ToShort() {
		user.setUsername("abcde");
		
		usernameValidationResult = userValidator.validateUsername(user);
		
		assertFalse(usernameValidationResult);
	}
	
	@Test
	public void wrongUsername_usernameIsNull() {
		user.setUsername(null);
		
		usernameValidationResult = userValidator.validateUsername(user);
		
		assertFalse(usernameValidationResult);
	}
	
	@Test
	public void wrongUsername_usernameExistInDatabase(){		
		user.setUsername("dobryusername");
		
		Mockito.when(userService.checkIfUsernameExist(user.getUsername()))
			.thenReturn(false);
		
		usernameValidationResult = userValidator.validateUsername(user);
		
		assertTrue(usernameValidationResult);
	}
	
	@Test
	public void properUsername_usernameIsOkAndDoesNotExistInDatabase() {
		user.setUsername("dobryusername");
		
		Mockito.when(userService.checkIfUsernameExist(user.getUsername()))
			.thenReturn(true);
		
		usernameValidationResult = userValidator.validateUsername(user);
		
		assertFalse(usernameValidationResult);
		Mockito.verify(userService, times(1)).checkIfUsernameExist("dobryusername");
	}
	
	@Test
	public void wrongEmail_withoutAtSign() {
		user.setEmail("dsadasdsa");
		assertFalse(userValidator.validateEmail(user));
	}
	
	@Test
	public void wrongEmail_withoutEnd() {
		user.setEmail("dsadasdsa@wp");
		assertFalse(userValidator.validateEmail(user));
	}
	
	@Test
	public void wrongEmail_toShortEnd() {
		user.setEmail("a@p.a");
		assertFalse(userValidator.validateEmail(user));
	}
	
	@Test
	public void wrongEmail_existInDatabase() {
		String email = "email@email.com";
		
		user.setEmail(email);
		
		Mockito.when(userService.checkIfEmailExist(email))
			.thenReturn(true);
		
		assertFalse(userValidator.validateEmail(user));
		Mockito.verify(userService, times(1)).checkIfEmailExist(email);
	}
	
	@Test
	public void properEmail() {
		String email = "email@email.com";
		user.setEmail(email);
		
		Mockito.when(userService.checkIfEmailExist(email))
			.thenReturn(false);
		
		assertTrue(userValidator.validateEmail(user));
		Mockito.verify(userService, times(1)).checkIfEmailExist(email);
	}
}
