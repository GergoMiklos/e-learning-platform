package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.Task;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class TaskSearchResultDto {

    private int totalPages;
    private Long totalElements;
    private List<Task> tasks;

    public static TaskSearchResultDto build(Page<Task> taskPage) {
        return TaskSearchResultDto.builder()
                .totalPages(taskPage.getTotalPages())
                .totalElements(taskPage.getTotalElements())
                .tasks(taskPage.getContent())
                .build();
    }

}
