package com.thesis.studyapp.graphql.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.service.UserService;
import graphql.execution.batched.Batched;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
public class GroupResolver implements GraphQLResolver<GroupDTO> {
    @Autowired
    UserService userService;

    @Autowired
    DataLoaderRegistry dataLoaderRegistry;

//LISTÁKRA NEM TUDOM BATChOLNI :(
    public CompletableFuture<List<UserDTO>> users(GroupDTO groupDTO) {
        if(groupDTO.getUsers() == null) {
            System.out.println("GroupResolver: users");
            DataLoader<Long, UserDTO> userloader = dataLoaderRegistry.getDataLoader("userloader");
            return userloader.loadMany(groupDTO.getUserIds());
        }
        else {
            return CompletableFuture.completedFuture(groupDTO.getUsers());
        }
    }

    public CompletableFuture<List<UserDTO>> admins(GroupDTO groupDTO) {
        System.out.println("GroupResolver: users");
        DataLoader<Long, UserDTO> userloader = dataLoaderRegistry.getDataLoader("userloader");
        return userloader.loadMany(groupDTO.getAdminIds());
    }

}
