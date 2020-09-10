package com.thesis.studyapp.dto;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
@Data
public class TestTaskDto implements HasId {

    Long id;

    private int level;

    private Long taskId;

}
