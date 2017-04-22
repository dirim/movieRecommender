package com.ozge.movieRecommender.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by ozge on 22.04.2017.
 */
@Entity
public class WatchingMovie {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	private User user;

	@ManyToOne(cascade = CascadeType.ALL)
	private Movie movie;

	public WatchingMovie() {
	}

	public WatchingMovie(User user, Movie movie) {
		this.user = user;
		this.movie = movie;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}
}
