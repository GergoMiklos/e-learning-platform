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

    private String name;
    private String description;

    public static TestDto build(Test test) {
        return TestDto.builder()
                .id(test.getId())
                .name(test.getName())
                .description(test.getDescription())
                .build();
    }
}
