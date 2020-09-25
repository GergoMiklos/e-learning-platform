package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.HasId;
import com.thesis.studyapp.model.TestTask;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;
import java.util.stream.Collectors;

@QueryResult
@Data
@Builder
public class TestTaskDto implements HasId {

    Long id;

    private int level;
    private int allSolutions;
    private int correctSolutions;

    private Long taskId;

    public static TestTaskDto build(TestTask testTask) {
        if (testTask.getTask() == null) {
            throw new IllegalStateException("Relationships needed for converting TestTask");
        }

        return TestTaskDto.builder()
                .id(testTask.getId())
                .level(testTask.getLevel())
                .allSolutions(testTask.getAllSolutions())
                .correctSolutions(testTask.getCorrectSolutions())
                .taskId(testTask.getTask().getId())
                .build();
    }

    public static List<TestTaskDto> build(List<TestTask> testTasks) {
        return testTasks.stream()
                .map(TestTaskDto::build)
                .collect(Collectors.toList());
    }

}
