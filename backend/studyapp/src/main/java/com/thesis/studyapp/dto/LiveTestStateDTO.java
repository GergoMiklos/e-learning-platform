package com.thesis.studyapp.dto;

import com.thesis.studyapp.HasId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;
import java.util.List;

@QueryResult
@Getter @Setter @NoArgsConstructor
public class LiveTestStateDTO implements HasId {

    private Long id;

//    private int currentLevel;
//    private int prevAvgLevel;
//    private State currentState;
//    private int correctsInRow;
//    private int failsInRow;

    private Date timeStartedTest;
    private Date timeStateChanged;

//    List<Long> completedTaskIds;
//    List<Long> failedTaskIds;

    private Long currentTaskId;
    private Long userId;
    private Long testId;

//    public enum State {
//        NOT_STARTED, IN_PROGRESS, INACTIVE, PROBLEM, FINISHED
//    }

//    private TaskDTO currentTask;
//    private UserDTO user;
//    private TestDTO test;
}
