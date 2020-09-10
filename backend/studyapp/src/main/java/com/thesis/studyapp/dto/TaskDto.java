package com.thesis.studyapp.dto;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@QueryResult
@Data
public class TaskDto implements HasId {

    private Long id;

    private String question;
    private List<String> answers;
    private int solution;

}
