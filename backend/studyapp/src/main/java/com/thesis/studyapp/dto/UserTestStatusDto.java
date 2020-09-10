package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.UserTestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;

@QueryResult
@Getter
@Setter
@NoArgsConstructor
public class UserTestStatusDto implements HasId {

    private Long id;

//    private int currentLevel;
//    private int prevAvgLevel;
//    private State currentState;
//    private int correctsInRow;
//    private int failsInRow;

    private UserTestStatus.Status status;
    private Date statusChangedTime;

//    List<Long> completedTaskIds;
//    List<Long> failedTaskIds;

    private Long currentTaskId;
    private Long userId;
    private Long testId;

//    private TaskDto currentTask;
//    private UserDto user;
//    private TestDto test;
}
