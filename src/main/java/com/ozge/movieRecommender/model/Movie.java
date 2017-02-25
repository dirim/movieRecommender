package com.ozge.movieRecommender.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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

	private int rate;

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

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
}
