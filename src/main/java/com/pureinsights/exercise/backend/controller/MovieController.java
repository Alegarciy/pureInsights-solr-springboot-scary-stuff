package com.pureinsights.exercise.backend.controller;

import com.pureinsights.exercise.backend.model.Movie;
import com.pureinsights.exercise.backend.model.MovieSolr;
import com.pureinsights.exercise.backend.repository.SolrMovieRepository;
import com.pureinsights.exercise.backend.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

/**
 * REST Controller for the search endpoints
 * @author Andres Marenco
 */
@Tag(name = "Movie")
@RestController("/movie")
public class MovieController {

  @Autowired
  private MovieService movieService;

  @Autowired
  private SolrMovieRepository solrMovieRepository;

  @Operation(summary = "Search the movie collection", description = "Executes a search of a movie in the collection")
  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Movie>> search(@RequestParam("q") String query, @ParameterObject Pageable pageRequest) {
    return ResponseEntity.ok(movieService.search(query, pageRequest));
  }

  @GetMapping(value = "/topRatedMovieForAllYears")
  public ResponseEntity<List<MovieSolr>> findAllByDate(@RequestParam(value = "Date", required = false) String dateInput) {
    Sort sort = Sort.by(Sort.Direction.DESC, "Rate");
    List<MovieSolr> topRatedMovies = new ArrayList<>();

    // If the request has a year, just get a single movie for that year
    if(dateInput != null) {
      List<MovieSolr> topMovie = solrMovieRepository.findTopRatedMovieForYear(Double.MAX_VALUE, Integer.parseInt(dateInput), PageRequest.of(0, 1,sort));
      topRatedMovies.add(topMovie.get(0));
      return ResponseEntity.ok(topRatedMovies);
    }

    // Else get the top movie for each year
    FacetPage<MovieSolr> response = solrMovieRepository.findAllByDate(PageRequest.of(0, 1000));
    Page<FacetFieldEntry> facetField = response.getFacetResultPage("Date");
    List<String> dateList = new ArrayList<>();

    for (FacetFieldEntry entry : facetField.getContent()) {
      String date = entry.getValue();
      dateList.add(date);
    }

    for(String date : dateList) {
      List<MovieSolr> topMovie = solrMovieRepository.findTopRatedMovieForYear(Double.MAX_VALUE, Integer.parseInt(date), PageRequest.of(0, 1,sort));
      topRatedMovies.add(topMovie.get(0));
    }

    return ResponseEntity.ok(topRatedMovies);
  }

  @GetMapping(value = "/topRatedGenre")
  public ResponseEntity<List<MovieSolr>> findTopRatedMoviesByGenre(@RequestParam("genre") String genre) {
    Sort sort = Sort.by(Sort.Direction.DESC, "Rate");
    return ResponseEntity.ok(solrMovieRepository.findTopRatedByGenre(genre,PageRequest.of(0, 10,sort)));
  }


}
