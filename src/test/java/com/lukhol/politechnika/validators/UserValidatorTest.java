package com.lukhol.politechnika.validators;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.lukhol.politechnika.models.User;
import com.lukhol.politechnika.services.UserService;

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
		user.setUsername("doryusername");
		
		Mockito.when(userService.checkIfUsernameExist(user.getUsername()))
			.thenReturn(true);
		
		usernameValidationResult = userValidator.validateUsername(user);
		
		assertFalse(usernameValidationResult);
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
	public void properEmail() {
		user.setEmail("dsadasdsa@wp.pl");
		assertTrue(userValidator.validateEmail(user));
	}
}
