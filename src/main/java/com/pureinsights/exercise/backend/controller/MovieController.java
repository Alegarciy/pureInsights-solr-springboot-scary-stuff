package com.pureinsights.exercise.backend.controller;

import com.pureinsights.exercise.backend.model.MovieSolr;
import com.pureinsights.exercise.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

/**
 * REST Controller for the search endpoints
 * @author Alejandro Garcia
 */
@Tag(name = "Movie")
@RestController("/movie")
public class MovieController {
  @Autowired
  private MovieService solrMovieRepositoryImpl;

  @Operation(summary = "Search the top-rated movie in a year, or all the top movies in a year for all the years", description = "Executes a search with a facet by years and sorted by rate of a or many movies in the collection")
  @GetMapping(value = "/topRatedMovieForAllYears", produces = "application/json")
  public ResponseEntity<List<MovieSolr>> findAllByDate(@RequestParam(value = "Date", required = false) String dateInput) {
    return ResponseEntity.ok(solrMovieRepositoryImpl.findAllByDate(dateInput));
  }

  @Operation(summary = "Search the top-rated movies filtered by genre", description = "Executes a search for genres and movies and then sorts it by the rate of each movie in the collection")
  @GetMapping(value = "/topRatedGenre", produces = "application/json")
  public ResponseEntity<List<MovieSolr>> findTopRatedMoviesByGenre(@RequestParam("genre") String genre, @RequestParam("amount") int amount) {
    return ResponseEntity.ok(solrMovieRepositoryImpl.findTopRatedMoviesByGenre(genre, amount));
  }


}
