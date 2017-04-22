package com.ozge.movieRecommender;

import com.ozge.movieRecommender.model.Movie;
import com.ozge.movieRecommender.repository.MovieRepository;
import com.ozge.movieRecommender.repository.UserRepository;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired UserRepository userRepository;

	@Autowired
	MovieRepository movieRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		OMDB omdb = Feign.builder().decoder(new GsonDecoder()).target(OMDB.class, "http://www.omdbapi.com/");
		MovieDB movieDB = Feign.builder().decoder(new GsonDecoder()).target(MovieDB.class, "https://api.themoviedb.org");

		IntStream.rangeClosed(1, 20).forEach(i -> {
			List<MovieDTO> idTitleList = movieDB.getMovies(i).getResults();
			idTitleList.forEach(r -> {
				Movie movie = new Movie();
				movie.setMovieName(r.getTitle());
				movie.setOverview(r.getOverview());

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
	}
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
	private String overview;

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

	public String getOverview() {
		return overview;
	}

	public void setOverview(String overview) {
		this.overview = overview;
	}
}

class OMDBMovie {
	private double imdbRating;
	private String Genre;
	private int Year;
	private String Poster;

	public int getYear() {
		return Year;
	}

	public void setYear(int year) {
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
