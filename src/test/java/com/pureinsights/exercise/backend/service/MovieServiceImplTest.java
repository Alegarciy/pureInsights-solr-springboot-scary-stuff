package com.pureinsights.exercise.backend.service;

import com.pureinsights.exercise.backend.model.MovieSolr;
import com.pureinsights.exercise.backend.repository.SolrMovieRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link MovieServiceImpl}
 * @author Andres Marenco
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

  @InjectMocks
  private MovieServiceImpl movieService;

  @Mock
  private SolrMovieRepositoryImpl movieRepository;

  @Test
  void findAllByDate() {
    // Test the case where dateInput is null
    List<MovieSolr> movies = movieService.findAllByDate(null);
    assertNotNull(movies);

    // Integrated test 1 - Test if the repository return the same as the service when a year is not given
    List<MovieSolr> moviesFromRepo = movieRepository.findTopRatedMovieYearly(null);
    assertEquals(movies, moviesFromRepo);

    // Test the case where dateInput is null
    movies = movieService.findAllByDate("2015");
    assertNotNull(movies);

    // Integrated test 2 - Test if the repository return the same as the service when a year is given
    moviesFromRepo = movieRepository.findTopRatedMovieForYear("2015");
    assertEquals(movies, moviesFromRepo);
  }

  @Test
  void findTopRatedMoviesByGenre() {

    // Test the case where findTopRatedMoviesByGenre is null
    List<MovieSolr> movies = movieService.findTopRatedMoviesByGenre("Action", 10);
    assertNotNull(movies);

    // Integrated test - Test if the repository return the same as the service when a genre is given
    List<MovieSolr> moviesFromRepo = movieRepository.findTopRatedByGenre("Action", 10);
    assertEquals(movies, moviesFromRepo);
  }




}
