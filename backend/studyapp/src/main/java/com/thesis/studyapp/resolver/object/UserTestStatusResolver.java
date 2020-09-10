package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserTestStatusResolver implements GraphQLResolver<UserTestStatusDto> {

    private final DataLoaderRegistry dataLoaderRegistry;

    public CompletableFuture<UserDto> user(UserTestStatusDto userTestStatusDto) {
        DataLoader<Long, UserDto> userLoader = dataLoaderRegistry.getDataLoader("userLoader");
        return userLoader.load(userTestStatusDto.getUserId());
    }

    public CompletableFuture<TestDto> test(UserTestStatusDto userTestStatusDto) {
        DataLoader<Long, TestDto> testLoader = dataLoaderRegistry.getDataLoader("testLoader");
        return testLoader.load(userTestStatusDto.getTestId());
    }

    public CompletableFuture<TaskDto> currentTask(UserTestStatusDto userTestStatusDto) {
        if (userTestStatusDto.getCurrentTaskId() != null) {
            DataLoader<Long, TaskDto> taskLoader = dataLoaderRegistry.getDataLoader("taskLoader");
            return taskLoader.load(userTestStatusDto.getCurrentTaskId());
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<String> statusChangedTime(UserTestStatusDto userTestStatusDto) {
        return CompletableFuture.supplyAsync(() -> userTestStatusDto.getStatusChangedTime().toString());
    }

    public CompletableFuture<String> status(UserTestStatusDto userTestStatusDto) {
        return CompletableFuture.supplyAsync(() -> userTestStatusDto.getStatus().name());
    }


}
