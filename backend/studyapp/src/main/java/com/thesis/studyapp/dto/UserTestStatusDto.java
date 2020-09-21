package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.UserTestStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.time.LocalDateTime;
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
    private LocalDateTime statusChangedTime;

//    List<Long> completedTaskIds;
//    List<Long> failedTaskIds;

    //todo currentTask nincs is!
    private Long currentTaskId;
    private Long userId;
    private Long testId;

    private int correctAnswers;
    private int allAnswers;

    public static UserTestStatusDto build(UserTestStatus userTestStatus) {
        if (userTestStatus.getTest() == null || userTestStatus.getUser() == null) {
            throw new IllegalStateException("Relationships needed for converting UserTestStatus!");
        }
        return UserTestStatusDto.builder()
                .id(userTestStatus.getId())
                .status(userTestStatus.getStatus())
                .statusChangedTime(userTestStatus.getStatusChangedDate())
                .testId(userTestStatus.getTest().getId())
                .userId(userTestStatus.getUser().getId())
                //todo ez nem kell, ezt majd a nexTask fogja kiszámolni??
                .currentTaskId(userTestStatus.getCurrentTask() != null ? userTestStatus.getCurrentTask().getId() : null)
                .correctAnswers(userTestStatus.getCorrectAnswers())
                .allAnswers(userTestStatus.getAllAnswers())
                .build();
    }
}
