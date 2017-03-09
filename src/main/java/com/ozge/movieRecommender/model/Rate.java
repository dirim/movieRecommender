package com.ozge.movieRecommender.model;

import javax.persistence.*;

/**
 * Created by ozge on 05.03.2017.
 */
@Entity
public class Rate {

	@Id
	@GeneratedValue
	private Long id;

	private int rate;

	@ManyToOne(cascade = CascadeType.ALL)
	private Movie movie;

	@ManyToOne(cascade = CascadeType.ALL)
	private User user;

	public Rate() {
	}

	public Rate(int rate, Movie movie, User user) {
		this.rate = rate;
		this.movie = movie;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
