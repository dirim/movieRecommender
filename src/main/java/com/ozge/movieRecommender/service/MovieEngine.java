package com.ozge.movieRecommender.service;

import com.ozge.movieRecommender.model.Movie;
import com.ozge.movieRecommender.repository.MovieRepository;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by ozge on 21.02.2017.
 */
@Service
public class MovieEngine {

	@Autowired
	private MovieRepository movieRepository;

	public Page<Movie> listAllByPage(Pageable pageable) {
		return movieRepository.findAll(pageable);
	}

	public List<OMDB> getRecommendedMovies(int movieId) {
		OMDB omdb = Feign.builder().decoder(new GsonDecoder()).target(OMDB.class, "http://www.omdbapi.com/");
		MovieDB movieDB = Feign.builder().decoder(new GsonDecoder()).target(MovieDB.class, "https://api.themoviedb.org");

		IntStream.rangeClosed(movieId, 20).forEach(i -> {
			List<MovieDTO> idTitleList = movieDB.getMovies(i).getResults();
			idTitleList.forEach(r -> {
				Movie movie = new Movie();
				movie.setMovieName(r.getTitle());

				String imdbId = movieDB.getIMDBId(r.getId()).getImdb_id();
				OMDBMovie movieDetails = omdb.getMovie(imdbId);
				movie.setImdbId(imdbId);
				movie.setImdbRate(movieDetails.getImdbRating());
				movie.setCatName(movieDetails.getGenre().split(",")[0]);
				movie.setYear(movieDetails.getYear());
				movie.setPoster(movieDetails.getPoster());
				movieRepository.save(movie);
			});
		});

		return Collections.singletonList(omdb);
	}

	public void generateRecommended() {
		getRecommendedMovies(1);
	}

	interface MovieDB {
		@RequestLine("GET /3/movie/top_rated?api_key=bdbca2d9594f7c83bd57ac4914388934&language=en-US&page={page}")
		Response getMovies(@Param("page") int page);

		@RequestLine("GET /3/movie/{movieId}?api_key=bdbca2d9594f7c83bd57ac4914388934&language=en-US")
		MovieIMDB getIMDBId(@Param("movieId") int movieId);
	}

	interface OMDB {
		@RequestLine("GET ?i={imdbId}")
		OMDBMovie getMovie(@Param("imdbId") String imdbId);
	}

	class MovieIMDB {
		private String imdb_id;

		public String getImdb_id() {
			return imdb_id;
		}

		public void setImdb_id(String imdb_id) {
			this.imdb_id = imdb_id;
		}
	}

	class MovieDTO {

		private int id;
		private String title;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	class OMDBMovie {
		private double imdbRating;
		private String Genre;
		private String Year;
		private String Poster;

		public String getYear() {
			return Year;
		}

		public void setYear(String year) {
			Year = year;
		}

		public String getPoster() {
			return Poster;
		}

		public void setPoster(String poster) {
			Poster = poster;
		}

		public String getGenre() {
			return Genre;
		}

		public void setGenre(String genre) {
			Genre = genre;
		}

		public double getImdbRating() {
			return imdbRating;
		}

		public void setImdbRating(double imdbRating) {
			this.imdbRating = imdbRating;
		}
	}

	class Response {
		List<MovieDTO> results;

		public List<MovieDTO> getResults() {
			return results;
		}

		public void setResults(List<MovieDTO> results) {
			this.results = results;
		}
	}

	public List<Movie> getRecommendedMovies(Movie movie) {
		List<Movie> recommendedMovies = new ArrayList<>();

		for (Movie m: movieRepository.findAll()) {
			if (m.getCatName().equals(movie.getCatName())) {
				if (!m.getId().equals(movie.getId())){
					recommendedMovies.add(m);
				}
			}
		}

		Collections.shuffle(recommendedMovies);
		recommendedMovies = recommendedMovies.subList(0, 3);

		List<Movie> movies = movieRepository.findByCatNameNot(movie.getCatName());
		Collections.shuffle(movies);
		recommendedMovies.addAll(movies.subList(0, 2));

		return recommendedMovies;
	}

}
