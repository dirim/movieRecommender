package com.ozge.movieRecommender.controller;

import com.ozge.movieRecommender.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

}
