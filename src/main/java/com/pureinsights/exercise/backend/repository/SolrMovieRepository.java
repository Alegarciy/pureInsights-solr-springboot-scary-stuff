package com.pureinsights.exercise.backend.repository;

import com.pureinsights.exercise.backend.model.MovieSolr;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;

import java.util.List;

@ComponentScan
public interface SolrMovieRepository extends PagingAndSortingRepository<MovieSolr, String> {

    @Query("Rate:[* TO ?0] AND Date:?1")
    List<MovieSolr> findTopRatedMovieForYear(double maxRating, int year, Pageable pageable);

    @Query("*:*")
    @Facet(fields = {"Date"}, limit = -1)
    FacetPage<MovieSolr> findAllByDate(Pageable pageable);

    @Query("Genre:\"?0\"")
    List<MovieSolr> findTopRatedByGenre(String genre,Pageable pageable);

}
