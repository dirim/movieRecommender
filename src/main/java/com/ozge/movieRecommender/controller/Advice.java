package com.ozge.movieRecommender.controller;

import com.ozge.movieRecommender.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class Advice {

	@ModelAttribute
	public void addUser(Model model, @AuthenticationPrincipal User user) {
		if (user != null) {
			model.addAttribute("user", user);
		}
	}
}
