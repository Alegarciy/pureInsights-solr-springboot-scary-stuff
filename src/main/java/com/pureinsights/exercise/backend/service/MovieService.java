package com.pureinsights.exercise.backend.service;

import com.pureinsights.exercise.backend.model.MovieSolr;

import java.util.List;

/**
 * A search service with methods to execute queries in the movie collection
 * @author Alejandro Garcia
 */
public interface MovieService {

  /**
   * Finds all movies for a given date, or the top-rated movie for each year if no date is provided.
   *
   * @param dateInput The date to search for in the format "yyyy-MM-dd", or null to search for the top rated movie for each year.
   * @return A ResponseEntity containing a list of MovieSolr objects.
   */
  List<MovieSolr> findAllByDate(String dateInput);

  /**
   * Finds the top n highest rated movies for a given genre.
   *
   * @param genre The genre to search for.
   * @return A list of up to n MovieSolr objects sorted by descending rating.
   */
  List<MovieSolr> findTopRatedMoviesByGenre(String genre, int amount);


}
