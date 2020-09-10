package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.TestTaskDto;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TestTaskResolver implements GraphQLResolver<TestTaskDto> {

    private final DataLoaderRegistry dataLoaderRegistry;

    public CompletableFuture<TaskDto> task(TestTaskDto testTaskDto) {
        DataLoader<Long, TaskDto> taskLoader = dataLoaderRegistry.getDataLoader("taskLoader");
        return taskLoader.load(testTaskDto.getTaskId());
    }


}
