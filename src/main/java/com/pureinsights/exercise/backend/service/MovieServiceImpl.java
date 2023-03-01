package com.pureinsights.exercise.backend.service;

import com.pureinsights.exercise.backend.model.MovieSolr;
import com.pureinsights.exercise.backend.repository.SolrMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link MovieService}
 * @author Alejandro Garcia
 */
@Service
public class MovieServiceImpl implements MovieService {
  @Autowired
  private SolrMovieRepository solrMovieRepository;

  public List<MovieSolr> findAllByDate(String dateInput) {
    Sort sort = Sort.by(Sort.Direction.DESC, "Rate");

    // Get the top-rated movie for the provided year
    if(dateInput != null) {
      return findTopRatedMovieByYear(dateInput, sort);
    }

    // Else get the Year for all the Years registered in the dataset
    return findTopRatedMovieYearly(sort);
  }

  /**
   * Retrieves the top movie one year, sorted by the given sort order.
   *
   * @param sort The sort order to apply DESC | ASC and by which Field.
   * @param dateInput The date input to use for filtering the results. (just the year)
   * @return A list of Movie objects, one for each year.
   */
  private List<MovieSolr> findTopRatedMovieByYear(String dateInput, Sort sort) {
    List<MovieSolr> topRatedMovies = new ArrayList<>();

    List<MovieSolr> topMovie = solrMovieRepository.findTopRatedMovieForYear(Double.MAX_VALUE, Integer.parseInt(dateInput), PageRequest.of(0, 1,sort));
    topRatedMovies.add(topMovie.get(0));

    return topRatedMovies;
  }

  /**
   * Finds the top-rated movie for each year, sorted by the given sort order.
   *
   * @param sort The sort order to apply.
   * @return A list of MovieSolr objects, one for each year.
   */
  private List<MovieSolr> findTopRatedMovieYearly(Sort sort) {
    List<MovieSolr> topRatedMovies = new ArrayList<>();
    List<String> yearList = new ArrayList<>();

    // Find the distinct years in the Solr index
    FacetPage<MovieSolr> response = solrMovieRepository.findAllByDate(PageRequest.of(0, 1));
    Page<FacetFieldEntry> facetField = response.getFacetResultPage("Date");

    // Add each distinct year to the yearList
    for (FacetFieldEntry entry : facetField.getContent()) {
      String date = entry.getValue();
      yearList.add(date);
    }

    // Find the top-rated movie for each year in the yearList
    for(String date : yearList) {
      List<MovieSolr> topMovie = solrMovieRepository.findTopRatedMovieForYear(Double.MAX_VALUE, Integer.parseInt(date), PageRequest.of(0, 1,sort));
      topRatedMovies.add(topMovie.get(0));
    }

    return topRatedMovies;
  }

  public List<MovieSolr> findTopRatedMoviesByGenre(String genre, int amount) {
    Sort sort = Sort.by(Sort.Direction.DESC, "Rate");
    return solrMovieRepository.findTopRatedByGenre(genre,PageRequest.of(0, amount,sort));
  }

}
