package com.thesis.studyapp.dto;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@QueryResult
public @Data class TestDTO {

    private Long id;

    private String name;
    private String description;

    private List<TaskDTO> tasks;
    private List<Long> taskIds;

}
