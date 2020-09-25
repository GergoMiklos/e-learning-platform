package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.HasId;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.model.UserTestTaskStatus;
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

    private UserTestStatus.Status status;
    private ZonedDateTime statusChangedTime;
    private int correctSolutions;
    private int allSolutions;
    private int solvedTasks;

    private Long userId;
    private Long testId;
    private List<Long> userTestTaskStatusIds;

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
                .userTestTaskStatusIds(uts.getUserTestTaskStatuses().stream()
                        .map(UserTestTaskStatus::getId).collect(Collectors.toList()))
                .build();
    }

}
