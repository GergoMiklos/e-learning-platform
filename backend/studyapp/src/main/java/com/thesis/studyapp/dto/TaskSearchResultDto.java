package com.thesis.studyapp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TaskSearchResultDto {

    private int totalPages;
    private Long totalElements;
    private List<TaskDto> tasks;

}
