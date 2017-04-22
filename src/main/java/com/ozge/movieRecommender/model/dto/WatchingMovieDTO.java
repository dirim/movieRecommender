package com.ozge.movieRecommender.model.dto;

/**
 * Created by ozge on 22.04.2017.
 */
public class WatchingMovieDTO {

	private Long userId;

	private Long movieId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
}
