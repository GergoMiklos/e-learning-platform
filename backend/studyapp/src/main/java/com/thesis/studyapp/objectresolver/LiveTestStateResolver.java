package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.*;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class LiveTestStateResolver implements GraphQLResolver<LiveTestStateDTO> {
    @Autowired
    DataLoaderRegistry dataLoaderRegistry;
    //TODO kell-e? Inkább DTO mappingnál húzd be a user name-t és code-ot

    public CompletableFuture<UserDTO> user(LiveTestStateDTO dto) {
        System.out.println("LiveTestUserState user");
        DataLoader<Long, UserDTO> userloader = dataLoaderRegistry.getDataLoader("userloader");
        return userloader.load(dto.getUserId());
    }

    public CompletableFuture<TestDTO> test(LiveTestStateDTO dto) {
        System.out.println("LiveTestUserState test");
        DataLoader<Long, TestDTO> testloader = dataLoaderRegistry.getDataLoader("testloader");
        return testloader.load(dto.getTestId());
    }

    public CompletableFuture<TaskDTO> currentTask(LiveTestStateDTO dto) {
        System.out.println("LiveTestUserState task");
        if(dto.getCurrentTaskId() != null) {
            DataLoader<Long, TaskDTO> taskloader = dataLoaderRegistry.getDataLoader("taskloader");
            return taskloader.load(dto.getCurrentTaskId());
        } else {
            return null;
        }
    }
}
