package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.Test;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
@Data
@Builder
public class TestDto implements HasId {

    private Long id;

    private Long groupId;

    private String name;
    private String description;
    private int allTasks;

    public static TestDto build(Test test) {
        if (test.getGroup() == null || test.getTestTasks() == null) {
            throw new IllegalStateException("Relationships needed for converting Test");
        }
        return TestDto.builder()
                .id(test.getId())
                .name(test.getName())
                .description(test.getDescription())
                .allTasks(test.getTestTasks().size())
                .groupId(test.getGroup().getId())
                .build();
    }
}
