package com.thesis.studyapp.dto;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;
import java.util.List;

@QueryResult
public @Data class LiveTestDTO {
    private Long id;;

    private Date creationDate;
    private int maxTimeForTest;
    private int maxTimeForTasks;

    private TestDTO test;
    private List<LiveTestStateDTO> liveTestStates;

    private Long testId;
    private List<Long> liveTestStateIds;


}
