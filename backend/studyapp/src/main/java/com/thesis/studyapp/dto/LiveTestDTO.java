package com.thesis.studyapp.dto;

import com.thesis.studyapp.HasId;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;
import java.util.List;

@QueryResult
public @Data class LiveTestDTO implements HasId {
    private Long id;;

    private Date creationDate;
//    private int maxTimeForTest;
//    private int maxTimeForTasks;

    private Long testId;
    private List<Long> liveTestStateIds;

//    private TestDTO test;
//    private List<LiveTestStateDTO> liveTestStates;
}
