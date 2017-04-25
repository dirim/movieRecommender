package com.ozge.movieRecommender.controller;

import com.ozge.movieRecommender.model.Movie;
import com.ozge.movieRecommender.model.Rate;
import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.model.dto.ProfileInfoDto;
import com.ozge.movieRecommender.model.validator.RegisterValidator;
import com.ozge.movieRecommender.repository.UserRepository;
import com.ozge.movieRecommender.service.UserService;
import org.apache.catalina.users.AbstractUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	private Environment environment;
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
	public String userProfile(Model model, @PathVariable("username") String username){

		//lazy initialization exception
		User user = userService.getUserByUsername(username);
		Set<Movie> watchedMovies =  user.getWatchedMovies();

		Map<String, Integer> nameRateMap = new HashMap<>();

		//create watched list for user
		for (Movie movie: watchedMovies) {
			for (Rate rate: movie.getRates()) {
				if (rate.getUser().getId().equals(user.getId())) {
					nameRateMap.put(movie.getMovieName(), rate.getRate());
					break;
				}
			}
		}

    	//recommends user
		List<User> similarUsers = new ArrayList<User>();
		for(Rate userRate: user.getRates()){
			for(Rate movieRate: userRate.getMovie().getRates()){
				if(userRate.getRate() == movieRate.getRate() ||
				   userRate.getRate()-1 == movieRate.getRate() ||
				   userRate.getRate()+1 == movieRate.getRate()){
					if(userRate.getUser().getId() != movieRate.getUser().getId()){
						similarUsers.add(movieRate.getUser());
					}

				}
			}
		}

		model.addAttribute("user", user);
		model.addAttribute("similarUsers", similarUsers);
		model.addAttribute("nameRateMap", nameRateMap);

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

	@RequestMapping(value = "/upload-avatar", method = RequestMethod.POST)
	public @ResponseBody
	ResponseEntity<String> uploadAvatar(@RequestParam("userAvatar") MultipartFile uploadedFile,
			Model model,
			@AuthenticationPrincipal User authenticatedUser){

		String path = environment.getProperty("upload.user.avatar.url");

		if (!uploadedFile.isEmpty() && checkIfFileIsImage(uploadedFile)) {
			try {
				byte[] bytes = uploadedFile.getBytes();
				UUID uuid = UUID.randomUUID();
				String filename = String.valueOf(uuid) + "." + getFileExtension(uploadedFile);
				File file = new File(path + filename);

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
				stream.write(bytes);
				stream.close();

				User user = this.userRepository.findById(authenticatedUser.getId());
				user.setAvatar(filename);
//				this.userService.update(user);
				this.userRepository.save(user);
				return new ResponseEntity(user.getAvatar(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity("You failed to upload because of " + e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity("You failed to upload avatar because the file was empty or not an image file.", HttpStatus.BAD_REQUEST);
		}

	}

	private String getFileExtension(MultipartFile file) {

		return file.getContentType().split("/")[1];

	}

	private boolean checkIfFileIsImage(MultipartFile file) {

		// TODO find all possible image mime types

		return file.getContentType().split("/")[0].equals("image");
	}


}
