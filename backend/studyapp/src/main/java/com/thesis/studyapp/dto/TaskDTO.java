package com.thesis.studyapp.dto;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.List;

public @Data class TaskDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String question;
    private List<String> answers;
    private int solution;
    private int level;

}
