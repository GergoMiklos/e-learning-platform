package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.TaskInputDto;
import com.thesis.studyapp.dto.TaskSearchResultDto;
import com.thesis.studyapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

@Component
@RequiredArgsConstructor
public class TaskEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TaskService taskService;

    public TaskSearchResultDto searchTasks(@Nullable String searchText, int page) {
        return taskService.searchTasks(searchText, page);
    }

    public TaskDto createTask(TaskInputDto taskInputDto) {
        return taskService.createTask(taskInputDto);
    }
}
