package com.pureinsights.exercise.backend.repository;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
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
public class SolrMovieRepositoryImpl {

    private static final String MOVIE_COLLECTION = "imdb.csv";
    private static final String[] FIELD_VALUES = {"Name", "Date", "Rate", "Votes", "Genre", "Duration", "Type", "Certificate", "Episodes", "Nudity", "Violence", "Profanity", "Alcohol", "Frightening"};
    private static final String SOLR_COLLECTION = "films";

    @Autowired
    private SolrClient solrClient; // Connects to Solr Client

    /**
     Creates a Solr collection named "films" with default config if it does not exist.
     @throws SolrServerException if there is an error executing the request
     @throws IOException if there is an error creating the collection
     */
    public void createSolrCollection() throws SolrServerException, IOException {

        // Create the collection
        CollectionAdminRequest.Create createRequest = CollectionAdminRequest.createCollection("films", "_default", 1, 1);
        createRequest.getParams();
        createRequest.process(solrClient);

        // Close the SolrClient instance
        solrClient.close();
    }

    /**
     Loads the movie data from the CSV file and indexes it in Solr.
     This method is executed after the creation of the bean.
     @throws IOException if there's an error reading the CSV file
     @throws SolrServerException if there's an error communicating with Solr
     */
    @PostConstruct
    public void postConstruct() throws IOException, SolrServerException {

        List<String> collectionNames = CollectionAdminRequest.listCollections(solrClient);

        // Check if collection exists
        if (collectionNames.contains(SOLR_COLLECTION)) {
            log.info("Collection films already instantiated", MOVIE_COLLECTION);
            return; // Collection already exists, exit method
        }

        log.info("Initializing solr films index with collection: {}", MOVIE_COLLECTION);
        // Create Solr Collection
        createSolrCollection();

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

    /**
     Transforms the given value based on the provided field name.
     If the field name is "Rate" and the value is "No Rate", it will return "0".
     If the field name is "Votes" and the value is "No Votes", it will return "0".
     Otherwise, it will return the original value.
     @param value the value to transform
     @param name the name of the field to transform
     @return the transformed value
     */
    public String transformValues(String value, String name) {
        if (name.equals("Rate")) {
            return Objects.equals(value, "No Rate") ? "0" : value;
        } else if (name.equals("Votes")) {
            return Objects.equals(value, "No Votes") ? "0" : value;
        }
        return value;
    }

}
