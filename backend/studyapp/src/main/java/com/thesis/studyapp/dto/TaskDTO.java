package com.thesis.studyapp.dto;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@QueryResult
public @Data class TaskDTO {

    private Long id;

    private String question;
    private List<String> answers;
    private int solution;
    private int level;

}
