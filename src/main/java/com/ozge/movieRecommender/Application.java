package com.ozge.movieRecommender;

import com.ozge.movieRecommender.model.Movie;
import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.repository.MovieRepository;
import com.ozge.movieRecommender.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Stream;

@SpringBootApplication
public class Application{

	@Autowired UserRepository userRepository;

	@Autowired
	MovieRepository movieRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	@Override
//	public void run(String... strings) throws Exception {
//
//		Movie movie1 = new Movie("Kelebegin Ruyasi", 2015, "Drama", 1L, 7.9);
//		this.movieRepository.save(movie1);
//		Movie movie2 = new Movie("Yüzüklerin Efendisi", 2002, "Adventure", 102L, 7.9);
//		this.movieRepository.save(movie2);
//		Movie movie3 = new Movie("Harry Potter", 2011, "Drama", 3001L, 10);
//		this.movieRepository.save(movie3);
//		Movie movie4 = new Movie("Hobbit", 2013, "Adventure", 521L, 8.2);
//		this.movieRepository.save(movie4);
//		Movie movie5 = new Movie("Leon", 2000, "Drama", 1L, 7.9);
//		this.movieRepository.save(movie5);
//	}


}
