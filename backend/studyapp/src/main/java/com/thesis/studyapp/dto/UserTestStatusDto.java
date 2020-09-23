package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.UserTestStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.time.ZonedDateTime;

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
    private ZonedDateTime statusChangedTime;

    //todo currentTask nincs is!
    private Long currentTaskId;
    private Long userId;
    private Long testId;

    private int correctAnswers;
    private int allAnswers;
    private int answeredTasks;

    public static UserTestStatusDto build(UserTestStatus uts) {
        if (uts.getTest() == null || uts.getUser() == null || uts.getTaskStatuses() == null) {
            throw new IllegalStateException("Relationships needed for converting UserTestStatus!");
        }
        return UserTestStatusDto.builder()
                .id(uts.getId())
                .status(uts.getStatus())
                .statusChangedTime(uts.getStatusChangedDate())
                .testId(uts.getTest().getId())
                .userId(uts.getUser().getId())
                //todo ez nem kell, ezt majd a nexTask fogja kiszámolni??
                //.currentTaskId(uts.getCurrentTestTask() == null ? null : uts.getCurrentTestTask().getId())
                .correctAnswers(uts.getCorrectAnswers())
                .allAnswers(uts.getAllAnswers())
                .answeredTasks(uts.getTaskStatuses().size())
                .build();
    }

    @Data
    @Builder
    public static class TaskStatusDto {
        private TestTask testTask;

        private int correctAnswers;
        private int allAnswers;
        private int correctAnswersInRow;
        private int wrongAnswersInRow;
    }
}
