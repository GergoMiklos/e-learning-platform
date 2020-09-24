package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.configuration.DateUtil;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserTestTaskStatusResolver implements GraphQLResolver<UserTestStatusDto.UserTestTaskStatusDto> {

    private final DataLoaderRegistry dataLoaderRegistry;

    public CompletableFuture<TestTaskDto> testTask(UserTestStatusDto.UserTestTaskStatusDto userTestTaskStatusDto) {
        DataLoader<Long, TestTaskDto> testTaskLoader = dataLoaderRegistry.getDataLoader("testTaskLoader");
        return testTaskLoader.load(userTestTaskStatusDto.getTestTaskId());
    }

    public CompletableFuture<String> lastSolutionTime(UserTestStatusDto.UserTestTaskStatusDto userTestTaskStatusDto) {
        return CompletableFuture.completedFuture(DateUtil.convertToIsoString(userTestTaskStatusDto.getLastSolutionTime()));
    }
}
