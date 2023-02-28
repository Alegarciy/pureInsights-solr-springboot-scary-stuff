package com.pureinsights.exercise.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@SpringBootApplication
@EnableSolrRepositories(basePackages = "com.pureinsights.exercise.backend.repository")
public class BackendTechnicalExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTechnicalExerciseApplication.class, args);
	}

}
