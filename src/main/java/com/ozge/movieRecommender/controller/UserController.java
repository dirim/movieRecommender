package com.ozge.movieRecommender.controller;

import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.model.dto.ProfileInfoDto;
import com.ozge.movieRecommender.model.validator.RegisterValidator;
import com.ozge.movieRecommender.repository.UserRepository;
import com.ozge.movieRecommender.service.UserService;
import org.apache.catalina.users.AbstractUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

//	private final UserService userService;
//	private final RegisterValidator registerValidator;
//
//	@Autowired
//	public UserController(UserService userService, RegisterValidator registerValidator) {
//		this.userService = userService;
//		this.registerValidator = registerValidator;
//	}
//
//	@InitBinder()
//	public void initBinder(WebDataBinder binder) {
//		binder.addValidators(registerValidator);
//	}

	@GetMapping("/register")
	public String register(Model model) {

		model.addAttribute("user", new User());
		return "user/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String createUser(@ModelAttribute @Valid User user, BindingResult bindingResult, HttpServletRequest request){

//		user.setPassword(passwordEncoder.encode(user.getPassword()));

		if(bindingResult.hasErrors()){
			return "user/register";
		}

		this.userRepository.save(user);
		return "redirect:/users/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginPage(){
		return "user/login";
	}


	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	public String userProfile(@PathVariable("username") String username,
								Model model){

		User user = this.userService.getUserByUsername(username);
		model.addAttribute("user", user);

		return "user/profile";
	}

	@RequestMapping(value = "/{username}/update", method = RequestMethod.GET)
	public String showUpdateForm(@PathVariable("username") String username,
									Model model,
									@AuthenticationPrincipal User authUser){

		User user = this.userService.getUserByUsername(username);

		if(authUser.getUsername().equals(user.getUsername())){

			ProfileInfoDto profileInfoDto = new ProfileInfoDto(user);
			model.addAttribute("user", profileInfoDto);

		}
		return "user/profileUpdate";
	}

	@RequestMapping(value = "/{username}/update", method = RequestMethod.POST)
	public String updateProfileInfo(@PathVariable("username") String username,
									@ModelAttribute ProfileInfoDto profileInfoDto,
									@AuthenticationPrincipal User authUser){

		User user = this.userService.getUserByUsername(username);

		if(authUser.getUsername().equals(user.getUsername())){
			User currentUser = profileInfoDto.toUser(user);
			this.userRepository.save(currentUser);
		}
		return "redirect:/users/" + profileInfoDto.getUsername();
	}

}
