package com.pureinsights.exercise.backend.repository;

import com.pureinsights.exercise.backend.model.MovieSolr;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class SolrMovieRepositoryTest {

    @Autowired
    private SolrMovieRepositoryImpl movieRepository;

    @Autowired
    private SolrTemplate solrTemplate;

    private static final String SOLR_COLLECTION = "films";

    @Test
    public void testFindTopRatedMovieForYear() {
        // Create some movies with different dates and rates
        List<MovieSolr> movies = new ArrayList<>();
        String[] genres = {"Action"};

        movies.add(createMovie("1", "Movie 1", 1700, 10.0, genres));
        movies.add(createMovie("2", "Movie 2", 1700, 8.0, genres));
        movies.add(createMovie("3", "Movie 3", 1700, 6.0, genres));
        movies.add(createMovie("4", "Movie 4", 1800, 9.0, genres));
        movies.add(createMovie("5", "Movie 5", 1900, 7.0, genres));
        movies.add(createMovie("6", "Movie 6", 1900, 5.0, genres));

        // Save the movies to the index
        solrTemplate.saveBeans(SOLR_COLLECTION, movies);
        solrTemplate.commit(SOLR_COLLECTION);

        // Call the method with different date inputs
        List<MovieSolr> result1 = movieRepository.findTopRatedMovieForYear("1700");
        List<MovieSolr> result2 = movieRepository.findTopRatedMovieForYear("1800");
        List<MovieSolr> result3 = movieRepository.findTopRatedMovieForYear("1900");

        // Verify that the expected movies are returned
        assertEquals(Arrays.asList(createMovie("1", "Movie 1", 1700, 10.0, genres)), result1);
        assertEquals(Arrays.asList(createMovie("4", "Movie 4", 1800, 9.0, genres)), result2);
        assertEquals(Arrays.asList(createMovie("5", "Movie 5", 1900, 7.0, genres)), result3);

        // Delete the movies from the index
        solrTemplate.deleteByIds(SOLR_COLLECTION, Arrays.asList("1", "2", "3", "4", "5", "6"));
        solrTemplate.commit(SOLR_COLLECTION);
    }

    @Test
    public void testFindAllDates() {
        Map yearHashmap = new HashMap();
        List<MovieSolr> movies = new ArrayList<>();
        String[] genres = {"Action"};

        // Create some movies with different dates and rates
        movies.add(createMovie("1", "Movie 1", 1100, 10.0, genres));
        movies.add(createMovie("2", "Movie 2", 1101, 8.0, genres));
        movies.add(createMovie("3", "Movie 3", 1102, 6.0, genres));
        movies.add(createMovie("4", "Movie 4", 1102, 4.0, genres));

        // Save the movies to the index
        solrTemplate.saveBeans(SOLR_COLLECTION, movies);
        solrTemplate.commit(SOLR_COLLECTION);

        // Get all the dates
        FacetPage<MovieSolr> facetPage = movieRepository.findAllByDate();
        Page<FacetFieldEntry> facetField = facetPage.getFacetResultPage("Date");

        // Add each distinct year to the yearList
        for (FacetFieldEntry entry : facetField.getContent()) {
            String date = entry.getValue();
            yearHashmap.put(date, true);
        }

        // Should instantiate the yearHashmap with the expected values in a different collection
        // TODO: Verify that the expected movies are returned
        // TODO: assertEquals(3, yearHashmap.size());

        // Verify if the years is in the list
        assertEquals(true, yearHashmap.containsKey("1100"));
        assertEquals(true, yearHashmap.containsKey("1101"));
        assertEquals(true, yearHashmap.containsKey("1102"));

        // Delete the movies from the index
        solrTemplate.deleteByIds(SOLR_COLLECTION, Arrays.asList("1", "2", "3", "4"));
    }

    @Test
    public void findTopRatedMovieYearly() {
        // Create some movies with different dates and rates
        List<MovieSolr> movies = new ArrayList<>();
        String[] genres = {"Action"};

        movies.add(createMovie("1", "Movie 1", 1100, 8.0, genres));
        movies.add(createMovie("2", "Movie 2", 1100, 7.0, genres));
        movies.add(createMovie("3", "Movie 3", 1101, 9.0, genres));
        movies.add(createMovie("4", "Movie 4", 1101, 8.0, genres));
        movies.add(createMovie("5", "Movie 5", 1102, 6.0, genres));
        movies.add(createMovie("6", "Movie 6", 1102, 9.0, genres));
        movies.add(createMovie("7", "Movie 7", 1102, 7.0, genres));
        movies.add(createMovie("8", "Movie 8", 1102, 8.0, genres));

        // Save the movies to the index
        solrTemplate.saveBeans(SOLR_COLLECTION, movies);
        solrTemplate.commit(SOLR_COLLECTION);

        // Call the function being tested
        Sort sort = Sort.by(Sort.Direction.DESC, "Rate");
        List<MovieSolr> topRatedMovies = movieRepository.findTopRatedMovieYearly(sort);

        // Verify that the expected movies are returned
        assertEquals("Movie 1", topRatedMovies.get(0).getName());
        assertEquals("Movie 3", topRatedMovies.get(1).getName());
        assertEquals("Movie 6", topRatedMovies.get(2).getName());

        // Delete the movies from the index
        solrTemplate.deleteByIds(SOLR_COLLECTION, Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));

    }

    @Test
    public void testFindTopRatedByGenre() {
        // Create some movies with different genres and rates
        List<MovieSolr> movies = new ArrayList<>();
        String[] genresAction = {"Action"};
        String[] genresDrama = {"Drama"};

        movies.add(createMovie("1", "Movie 1", 2020, 9.9, genresAction));
        movies.add(createMovie("2", "Movie 2", 2020, 9.9, genresDrama));
        movies.add(createMovie("3", "Movie 3", 2020, 7.0, genresAction));
        movies.add(createMovie("4", "Movie 4", 2021, 11.0, genresAction));
        movies.add(createMovie("5", "Movie 5", 2021, 6.0, genresAction));
        movies.add(createMovie("6", "Movie 6", 2021, 8.0, genresAction));

        // Save the movies to the index
        solrTemplate.saveBeans(SOLR_COLLECTION, movies);
        solrTemplate.commit(SOLR_COLLECTION);

        // Find the top 2 rated Action movies
        List<MovieSolr> topRatedActionMovies = movieRepository.findTopRatedByGenre("Action", 2);

        // Verify that the expected movies are returned
        assertEquals("Movie 4", topRatedActionMovies.get(0).getName());
        assertEquals("Movie 1", topRatedActionMovies.get(1).getName());

        // Find the top 1 rated Drama movie
        List<MovieSolr> topRatedDramaMovie = movieRepository.findTopRatedByGenre("Drama", 1);
        assertEquals("Movie 2", topRatedDramaMovie.get(0).getName());

        // Delete the movies from the index
        solrTemplate.deleteByIds(SOLR_COLLECTION, Arrays.asList("1", "2", "3", "4", "5", "6"));
    }

    private MovieSolr createMovie(String id, String name, int date, double rate, String[] genre) {
        MovieSolr movie = new MovieSolr();
        movie.setId(id);
        movie.setName(name);
        movie.setDate(date);
        movie.setRate(rate);
        movie.setGenre(genre);
        return movie;
    }

}
