package com.ozge.movieRecommender.service;

import com.ozge.movieRecommender.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ozge on 21.02.2017.
 */
@Service
public class MovieService {

	@Autowired
	private MovieRepository movieRepository;
}
