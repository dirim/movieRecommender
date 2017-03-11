package com.ozge.movieRecommender.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ozge on 21.02.2017.
 */
@Entity
public class Movie {

	@Id
	@GeneratedValue
	private Long id;

	private String movieName;

	private int year;

	private String catName;

	private Long imdbId;

	private double imdbRate;

	private double avgRate;

	@ManyToMany(cascade = CascadeType.ALL)
	private Set<User> users = new HashSet<>();

	@OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
	private List<Rate> rates;

	public Movie() {
	}

	public Movie(String movieName, int year, String catName, Long imdbId, double imdbRate) {
		this.movieName = movieName;
		this.year = year;
		this.catName = catName;
		this.imdbId = imdbId;
		this.imdbRate = imdbRate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Long getImdbId() {
		return imdbId;
	}

	public void setImdbId(Long imdbId) {
		this.imdbId = imdbId;
	}

	public double getImdbRate() {
		return imdbRate;
	}

	public void setImdbRate(double imdbRate) {
		this.imdbRate = imdbRate;
	}

	public double getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(double avgRate) {
		this.avgRate = avgRate;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public List<Rate> getRates() {
		return rates;
	}

	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}
}
