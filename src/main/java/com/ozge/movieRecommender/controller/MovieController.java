package com.ozge.movieRecommender.controller;

import com.ozge.movieRecommender.model.Movie;
import com.ozge.movieRecommender.model.Rate;
import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.model.WatchingMovie;
import com.ozge.movieRecommender.model.dto.RateDTO;
import com.ozge.movieRecommender.model.dto.WatchingMovieDTO;
import com.ozge.movieRecommender.repository.MovieRepository;
import com.ozge.movieRecommender.repository.RateRepository;
import com.ozge.movieRecommender.repository.UserRepository;
import com.ozge.movieRecommender.repository.WatchingMovieRepository;
import com.ozge.movieRecommender.service.MovieEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by ozge on 21.02.2017.
 */
@Controller
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	MovieRepository movieRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RateRepository rateRepository;

	@Autowired MovieEngine movieEngine;

	@Autowired
	WatchingMovieRepository watchingMovieRepository;

	@RequestMapping(value="",method=RequestMethod.GET)
	public String list(Model model, Pageable pageable){
		Page<Movie> movies = movieEngine.listAllByPage(pageable);
		model.addAttribute("movies", movies);
		model.addAttribute("size", movies.getTotalPages());
		return "movie/movieList";
	}

	@GetMapping("/{id}")
	public String showMovie(Model model, @AuthenticationPrincipal User user, @PathVariable("id") Long id){
		Movie movie = movieRepository.findOne(id);

		List<Rate> userRates = movie.getRates()
				.stream()
				.filter(rate -> rate.getUser().getId().equals(user.getId()))
				.collect(Collectors.toList());

		double userRate = 0;

		if (!(userRates.size() == 0)) { // if user voted
			userRate = userRates.get(0).getRate();
		}

		model.addAttribute("catMovies", movieEngine.getRecommendedMovies(movie));

		model.addAttribute("movie", movie);
		model.addAttribute("userRate", userRate);
		return "movie/movieDetail";
	}

	@PostMapping(value = "/{id}/rates", produces = "application/json")
	@ResponseBody
	public double rateMovie(@PathVariable("id") Long id, @RequestBody RateDTO rateDto) {
		//getting object from js is stored in RateDTO
		Movie movie = movieRepository.findOne(rateDto.getMovieId());
		User user = userRepository.findOne(rateDto.getUserId());
		List<Rate> rates = movie.getRates();

		boolean userVotedBefore = false;

		for (Rate r: rates) {
			if (r.getUser().getId().equals(user.getId())) {
				userVotedBefore = true;
				r.setRate(rateDto.getRate());
				rateRepository.save(r);
				break;
			}
		}

		if (!userVotedBefore) {
			Rate rate = new Rate(rateDto.getRate(), movie, user);
			rateRepository.save(rate);
			rates.add(rate);
			Set<User> watchedUsers = movie.getWatchedUsers();
			watchedUsers.add(user);
			movie.setWatchedUsers(watchedUsers);
			movieRepository.save(movie);
			Set<Movie> watchedMovies = user.getWatchedMovies();
			watchedMovies.add(movie);
			user.setWatchedMovies(watchedMovies);
			userRepository.save(user);
		}

		movie.setRates(rates);

		if (movie.getAvgRate() == 0) {
			return rateDto.getRate();
		}

		return ((movie.getAvgRate() * (movie.getRates().size() - 1)) + rateDto.getRate()) / movie.getRates().size();
	}

	@PostMapping(value = "/{id}/watchingList", produces = "application/json")
	@ResponseBody
	public WatchingMovie addWatchingMovie(@PathVariable("id") Long id, @RequestBody WatchingMovieDTO watchingMovieDTO) {
		Movie movie = movieRepository.findOne(watchingMovieDTO.getMovieId());
		User user = userRepository.findOne(watchingMovieDTO.getUserId());
		WatchingMovie watchingMovie = new WatchingMovie(user, movie);
		watchingMovieRepository.save(watchingMovie);

		return watchingMovie;
	}

}
