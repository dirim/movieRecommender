package com.ozge.movieRecommender.controller;

import com.ozge.movieRecommender.model.Movie;
import com.ozge.movieRecommender.model.Rate;
import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ozge on 21.02.2017.
 */
@Controller
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	MovieRepository movieRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String listMovies(Model model){

		model.addAttribute("movies", this.movieRepository.findAll());
		return "movie/movieList";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String showMovie(Model model, @PathVariable("id") Long id){

		model.addAttribute("movie", this.movieRepository.findOne(id));
		return "movie/movieDetail";
	}

//	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
//	public int showMovieRating(@PathVariable("id") Long id){
//
//		Movie movie = this.movieRepository.findOne(id);
//		List<Rate> rates = movie.getRates();
////		rates.add();
//		movie.setRates(rates);
//
//
//	}

}
