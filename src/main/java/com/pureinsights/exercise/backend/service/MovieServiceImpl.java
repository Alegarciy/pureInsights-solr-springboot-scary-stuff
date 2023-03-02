package com.pureinsights.exercise.backend.service;

import com.pureinsights.exercise.backend.model.MovieSolr;
import com.pureinsights.exercise.backend.repository.SolrMovieRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default implementation of {@link MovieService}
 * @author Alejandro Garcia
 */
@Service
public class MovieServiceImpl implements MovieService {
  @Autowired
  private SolrMovieRepositoryImpl solrMovieRepositoryImpl;

  public List<MovieSolr> findAllByDate(String dateInput) {
    Sort sort = Sort.by(Sort.Direction.DESC, "Rate");

    // Get the top-rated movie for the provided year
    if(dateInput != null) {
      return solrMovieRepositoryImpl.findTopRatedMovieForYear(dateInput);
    }

    // Else get the Year for all the Years registered in the dataset
    return solrMovieRepositoryImpl.findTopRatedMovieYearly(sort);
  }

  public List<MovieSolr> findTopRatedMoviesByGenre(String genre, int amount) {
    return solrMovieRepositoryImpl.findTopRatedByGenre(genre,amount);
  }

}
