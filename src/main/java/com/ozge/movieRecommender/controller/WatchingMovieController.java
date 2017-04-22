package com.ozge.movieRecommender.controller;

import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.model.WatchingMovie;
import com.ozge.movieRecommender.repository.WatchingMovieRepository;
import com.ozge.movieRecommender.service.WatchingMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by ozge on 22.04.2017.
 */
@Controller
@RequestMapping("/watchings")
public class WatchingMovieController {

	@Autowired
	private WatchingMovieRepository watchingMovieRepository;

	@Autowired
	private WatchingMovieService watchingMovieService;

	@GetMapping("")
	public String list(Model model,  @AuthenticationPrincipal User user){

		List<WatchingMovie> w = this.watchingMovieService.getWatchingMovieByUser(user);

		model.addAttribute("watchingMovies", w);
		return "movie/watchingList";
	}
}
