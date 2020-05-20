package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.*;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@Component
public class LiveTestStateResolver implements GraphQLResolver<LiveTestStateDTO> {
    @Autowired
    DataLoaderRegistry dataLoaderRegistry;

    public CompletableFuture<UserDTO> user(LiveTestStateDTO dto) {
        if(dto.getUserId() != null) {
            DataLoader<Long, UserDTO> userloader = dataLoaderRegistry.getDataLoader("userloader");
            return userloader.load(dto.getUserId());
        } else {
            return null;
        }
    }

    public CompletableFuture<TestDTO> test(LiveTestStateDTO dto) {
        if(dto.getTestId() != null) {
            DataLoader<Long, TestDTO> testloader = dataLoaderRegistry.getDataLoader("testloader");
            return testloader.load(dto.getTestId());
        } else {
            return null;
        }
    }

    public CompletableFuture<TaskDTO> currentTask(LiveTestStateDTO dto) {
        if(dto.getCurrentTaskId() != null) {
            DataLoader<Long, TaskDTO> taskloader = dataLoaderRegistry.getDataLoader("taskloader");
            return taskloader.load(dto.getCurrentTaskId());
        } else {
            return null;
        }
    }

    public CompletableFuture<Long> sinceStateRefreshMins(LiveTestStateDTO dto) {
        return CompletableFuture.supplyAsync(() -> {
            Date now = new Date();
            Date timeStateChanged = dto.getTimeStateChanged();
            if(timeStateChanged != null)
                return Duration.between(timeStateChanged.toInstant(), now.toInstant()).toMinutes();
            else
                return null;
        });
    }

    public CompletableFuture<Long> sinceStartMins(LiveTestStateDTO dto) {
        return CompletableFuture.supplyAsync(() -> {
            Date now = new Date();
            Date timeStarted = dto.getTimeStartedTest();
            if(timeStarted != null)
                return Duration.between(timeStarted.toInstant(), now.toInstant()).toMinutes();
            else
                return null;
        });
    }

}
