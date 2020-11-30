package com.thesis.studyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories("com.thesis.studyapp.repository")
@EntityScan(basePackages = "com.thesis.studyapp.model")
public class StudyappApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyappApplication.class, args);
    }

}
