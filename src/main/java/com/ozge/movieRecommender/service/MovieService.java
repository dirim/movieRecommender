package com.ozge.movieRecommender.service;

import com.ozge.movieRecommender.model.Movie;
import com.ozge.movieRecommender.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ozge on 21.02.2017.
 */
@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;

	public Page<Movie> listAllByPage(Pageable pageable) {
		return movieRepository.findAll(pageable);
	}

}
