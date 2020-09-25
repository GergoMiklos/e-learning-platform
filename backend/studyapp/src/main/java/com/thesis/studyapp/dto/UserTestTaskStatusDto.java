package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.HasId;
import com.thesis.studyapp.model.UserTestTaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class UserTestTaskStatusDto implements HasId {
    private Long id;

    private Long testTaskId;

    private ZonedDateTime lastSolutionTime;
    private int correctSolutions;
    private int allSolutions;

    public static UserTestTaskStatusDto build(UserTestTaskStatus utts) {
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

    public static List<UserTestTaskStatusDto> build(List<UserTestTaskStatus> userTestTaskStatuses) {
        return userTestTaskStatuses.stream()
                .map(UserTestTaskStatusDto::build)
                .collect(Collectors.toList());
    }
}
