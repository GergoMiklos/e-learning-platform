package com.thesis.studyapp.dto;

import com.thesis.studyapp.HasId;
import com.thesis.studyapp.model.Test;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@QueryResult
public @Data class TestTaskDTO implements HasId {

    Long id;

    private int level;

    private String question;
    private List<String> answers;

}
