package com.pureinsights.exercise.backend.service;

import com.pureinsights.exercise.backend.model.MovieSolr;
import com.pureinsights.exercise.backend.repository.SolrMovieRepositoryImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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


}
