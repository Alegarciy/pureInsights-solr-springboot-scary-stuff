package com.pureinsights.exercise.backend.configuration;

import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;

@Configuration
public class SolrConfig {

    @Autowired
    private SolrClient solrClient;

    @Bean
    public SolrTemplate solrTemplate() {
        return new SolrTemplate(solrClient);
    }
}