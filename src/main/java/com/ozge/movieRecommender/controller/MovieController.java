package com.ozge.movieRecommender.controller;

import com.ozge.movieRecommender.model.Movie;
import com.ozge.movieRecommender.model.Rate;
import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.model.dto.RateDTO;
import com.ozge.movieRecommender.repository.MovieRepository;
import com.ozge.movieRecommender.repository.RateRepository;
import com.ozge.movieRecommender.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String listMovies(Model model){
		model.addAttribute("movies", this.movieRepository.findAll());
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

		//recommends movies
		List<Movie> catMovies = new ArrayList<Movie>();

		for(Movie m: movieRepository.findAll()){
			if(m.getCatName().equals(movie.getCatName())){
				if(m.getId() != movie.getId()){
					catMovies.add(m);
				}
			}
		}


		model.addAttribute("catMovies", catMovies);

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

}
