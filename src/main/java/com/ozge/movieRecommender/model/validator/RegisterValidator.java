package com.ozge.movieRecommender.model.validator;

import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by ozge on 19.02.2017.
 */
@Component
public class RegisterValidator implements Validator {
	private final UserService userService;

	@Autowired
	public RegisterValidator(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return aClass.equals(User.class);
	}

	@Override
	public void validate(Object o, Errors errors) {
		User form = (User) o;
		validateUsernameAndEmail(errors, form);
	}

	private void validateUsernameAndEmail(Errors errors, User form) {
		if (userService.getUserByUsername(form.getUsername()) != null) {
			errors.rejectValue("username", "username.exists", "User with this username already exists");
		}

		if (userService.getUserByEmail(form.getEmail()) != null) {
			errors.rejectValue("email", "email.exists", "This email already exists, enter different one");
		}
	}
}
