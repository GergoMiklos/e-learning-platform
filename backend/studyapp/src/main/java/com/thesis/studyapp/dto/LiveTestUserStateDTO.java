package com.thesis.studyapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;
import java.util.List;

@QueryResult
@Getter @Setter @NoArgsConstructor
public class LiveTestUserStateDTO {

    private Long id;

    private int currentLevel;
    private int prevAvgLevel;
    private State currentState;
    private int correctsInRow;
    private int failsInRow;

    private Date timeStartedTest;
    private Date timeStartedLastTask;

    List<Long> completedTaskIds;
    List<Long> failedTaskIds;

    TaskDTO currentTask;
    private UserDTO user;
    LiveTestDTO liveTest;




    public enum State {
        NOT_STARTED, PROBLEM, FINISHED, IN_PROGRESS
    }

}
