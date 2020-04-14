package com.thesis.studyapp.model;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.List;

@NodeEntity
public @Data class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String question;
    private List<String> answers;
    private int solution;
    private int level;

}
