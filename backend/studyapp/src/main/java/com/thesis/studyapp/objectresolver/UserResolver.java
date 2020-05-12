package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.dto.UserDTO;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class UserResolver implements GraphQLResolver<UserDTO> {
    @Autowired
    DataLoaderRegistry dataLoaderRegistry;

    public CompletableFuture<List<GroupDTO>> groups(UserDTO userDTO) {
        DataLoader<Long, GroupDTO> grouploader = dataLoaderRegistry.getDataLoader("grouploader");
        return grouploader.loadMany(userDTO.getGroupIds());
    }

    public CompletableFuture<List<GroupDTO>> managedGroups(UserDTO userDTO) {
        DataLoader<Long, GroupDTO> grouploader = dataLoaderRegistry.getDataLoader("grouploader");
        return grouploader.loadMany(userDTO.getManagedGroupIds());
    }

    public CompletableFuture<List<TaskDTO>> createdTasks(UserDTO userDTO) {
        //Todo loader / külön / ne is legyen?
        return null;
    }

    public CompletableFuture<List<TestDTO>> createdTests(UserDTO userDTO) {
        if(userDTO.getCreatedTestIds() != null) {
            DataLoader<Long, TestDTO> testloader = dataLoaderRegistry.getDataLoader("testloader");
            return testloader.loadMany(userDTO.getCreatedTestIds());
        } else {
            return null;
        }
    }
}
