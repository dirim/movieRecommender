package com.ozge.movieRecommender.repository;

import com.ozge.movieRecommender.model.User;
import com.ozge.movieRecommender.model.WatchingMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ozge on 22.04.2017.
 */
@Repository
public interface WatchingMovieRepository extends JpaRepository<WatchingMovie, Long>{

	List<WatchingMovie> findByUser(User user);
}
