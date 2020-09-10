package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.UserTestStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;

@QueryResult
@Data
@Builder
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

    public static UserTestStatusDto build(UserTestStatus userTestStatus) {
        if (userTestStatus.getTest() == null || userTestStatus.getUser() == null) {
            throw new IllegalStateException("Relationships needed when converting to UserTestStatusDto!");
        }
        return UserTestStatusDto.builder()
                .id(userTestStatus.getId())
                .status(userTestStatus.getStatus())
                .statusChangedTime(userTestStatus.getStatusChangedDate())
                .testId(userTestStatus.getTest().getId())
                .userId(userTestStatus.getUser().getId())
                .currentTaskId(userTestStatus.getCurrentTask() != null ? userTestStatus.getCurrentTask().getId() : null)
                .build();
    }
}
