package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.TaskInputDto;
import com.thesis.studyapp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TaskService taskService;

    public List<TaskDto> tasks(String searchString) {
        return taskService.getTasks(searchString);
    }

    public TaskDto createTask(TaskInputDto taskInputDto) {
        return taskService.createTask(taskInputDto);
    }
}
