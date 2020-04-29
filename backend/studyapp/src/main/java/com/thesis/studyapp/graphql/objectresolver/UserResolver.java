package com.thesis.studyapp.graphql.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.graphql.queryresolver.GroupQuery;
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
        //TODO ha van request élető usercontext akkor itt vizsgál és külön kérdezed!
        DataLoader<Long, GroupDTO> grouploader = dataLoaderRegistry.getDataLoader("grouploader");
        return grouploader.loadMany(userDTO.getManagedGroupIds());
    }

    public CompletableFuture<List<TaskDTO>> createdTasks(UserDTO userDTO) {
        //Todo loader vagy külön vagy ne is legyen?
        return null;
    }

    public CompletableFuture<List<TestDTO>> createdTests(UserDTO userDTO) {
        //Todo
        return null;
    }
}
