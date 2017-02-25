package com.ozge.movieRecommender.service;

import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by ozge on 19.02.2017.
 */
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = getUserByUsername(username);

		if (null == user) {
			throw new UsernameNotFoundException("User with username: " + username + " not found.");
		} else {
			return user;
		}
	}

}
