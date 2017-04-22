package com.ozge.movieRecommender.service;

import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.model.WatchingMovie;
import com.ozge.movieRecommender.repository.WatchingMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ozge on 22.04.2017.
 */
@Service
public class WatchingMovieService {

	@Autowired
	private WatchingMovieRepository watchingMovieRepository;

	public List<WatchingMovie> getWatchingMovieByUser(User user){
		return watchingMovieRepository.findByUser(user);
	}
}
