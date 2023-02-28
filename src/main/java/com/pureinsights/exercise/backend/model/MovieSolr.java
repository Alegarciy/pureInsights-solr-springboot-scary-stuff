package com.pureinsights.exercise.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.SolrDocument;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SolrDocument(collection = "films")
public class MovieSolr {
    @Id
    @Field
    private String id;
    @Field
    private String Name;
    @Field
    private int Date;
    @Field
    private Double Rate;
    @Field
    private String[] Genre;
}
