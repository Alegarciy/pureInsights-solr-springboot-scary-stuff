package com.pureinsights.exercise.backend.repository;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * In-memory implementation of {@link SolrMovieRepository}
 * @author Alejandro Garcia
 */
@Repository
@Slf4j
public class ImplementationSolrMovieRepository{

    private static final String MOVIE_COLLECTION = "imdb.csv";
    private static final String[] FIELD_VALUES = {"Name", "Date", "Rate", "Votes", "Genre", "Duration", "Type", "Certificate", "Episodes", "Nudity", "Violence", "Profanity", "Alcohol", "Frightening"};
    private static final String SOLR_COLLECTION = "films";

    @Autowired
    private SolrClient solrClient; // Connects to Solr Client

    @PostConstruct
    public void postConstruct() throws IOException, SolrServerException {

        // Read CSV data
        CSVReader moviesDataset = new CSVReader(new InputStreamReader(Objects.requireNonNull(getClass()
                .getClassLoader()
                .getResourceAsStream(MOVIE_COLLECTION)
        )));
        moviesDataset.skip(1);

        // Create SolrInputDocuments
        List<SolrInputDocument> documents = new ArrayList<>();
        for (String[] row : moviesDataset) {

            SolrInputDocument document = new SolrInputDocument();
            int rowValueIndex = 0;

            for (String fieldName :FIELD_VALUES) {
                String fieldValues = transformValues(row[rowValueIndex++],fieldName);
                document.addField(fieldName, fieldValues);
            }

            documents.add(document);
        }
        // Perform bulk index
        solrClient.add(SOLR_COLLECTION, documents);
        solrClient.commit(SOLR_COLLECTION);
    }

    public String transformValues(String value, String name) {
        if (name.equals("Rate")) {
            return Objects.equals(value, "No Rate") ? "0" : value;
        }
        return value;
    }



}
