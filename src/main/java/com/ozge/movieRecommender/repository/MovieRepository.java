package com.ozge.movieRecommender.repository;

import com.ozge.movieRecommender.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ozge on 21.02.2017.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
