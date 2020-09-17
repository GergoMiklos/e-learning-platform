package com.thesis.studyapp.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.Depth;

import java.util.List;

@Data
@Builder
public class TaskSearchResultDto {

    private int totalPages;
    private Long totalElements;
    private List<TaskDto> tasks;

}
