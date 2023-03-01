package com.pureinsights.exercise.backend.repository;

import com.pureinsights.exercise.backend.model.MovieSolr;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;

import java.util.List;

@ComponentScan
public interface SolrMovieRepository extends Repository<MovieSolr, String> {

    /**
     * Finds the top-rated movies of a given genre using Solr search.
     *
     * @param genre the genre of the movies to retrieve
     * @param amount the maximum number of movies to retrieve
     * @return a list of the top-rated movies of the given genre
     */
    List<MovieSolr> findTopRatedByGenre(String genre, int amount);

    /**
     * Finds the top-rated movie for each year, sorted by the given sort order.
     *
     * @param sort The sort order to apply.
     * @return A list of MovieSolr objects, one for each year.
     */
    List<MovieSolr> findTopRatedMovieYearly(Sort sort);

    /**
     * Retrieves the top movie one year, sorted by the given sort order.
     *
     * @param dateInput The date input to use for filtering the results. (just the year)
     * @return A list of Movie objects, one for each year.
     */
     List<MovieSolr> findTopRatedMovieForYear(String dateInput);

    /**
     * Finds the different years in which MovieSolr were released.
     *
     * @return A list of all MovieSolr Dates, one for each different year.
     */
    FacetPage<MovieSolr> findAllByDate();

}
