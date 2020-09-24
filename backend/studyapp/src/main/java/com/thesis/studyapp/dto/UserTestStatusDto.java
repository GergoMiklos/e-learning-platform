package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.UserTestStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private Long userId;
    private Long testId;

    private int correctSolutions;
    private int allSolutions;
    private int solvedTasks;

    public static UserTestStatusDto build(UserTestStatus uts) {
        if (uts.getTest() == null || uts.getUser() == null || uts.getUserTestTaskStatuses() == null) {
            throw new IllegalStateException("Relationships needed for converting UserTestStatus!");
        }

        return UserTestStatusDto.builder()
                .id(uts.getId())
                .status(uts.getStatus())
                .statusChangedTime(uts.getStatusChangedDate())
                .testId(uts.getTest().getId())
                .userId(uts.getUser().getId())
                .correctSolutions(uts.getCorrectSolutions())
                .allSolutions(uts.getAllSolutions())
                .solvedTasks(uts.getUserTestTaskStatuses().size())
                .build();
    }

    @Data
    @Builder
    public static class UserTestTaskStatusDto implements HasId {
        private Long id;

        private Long testTaskId;

        private ZonedDateTime lastSolutionTime;
        private int correctSolutions;
        private int allSolutions;

        public static UserTestTaskStatusDto build(UserTestStatus.UserTestTaskStatus utts) {
            if (utts.getTestTask() == null) {
                throw new IllegalStateException("Relationships needed for converting UserTestTaskStatus!");
            }

            return UserTestTaskStatusDto.builder()
                    .id(utts.getId())
                    .testTaskId(utts.getTestTask().getId())
                    .lastSolutionTime(utts.getLastSolutionTime())
                    .correctSolutions(utts.getCorrectSolutions())
                    .allSolutions(utts.getAllSolutions())
                    .build();
        }

        public static List<UserTestTaskStatusDto> build(List<UserTestStatus.UserTestTaskStatus> userTestTaskStatuses) {
            return userTestTaskStatuses.stream()
                    .map(UserTestStatusDto.UserTestTaskStatusDto::build)
                    .collect(Collectors.toList());
        }
    }
}
