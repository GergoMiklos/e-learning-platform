package com.thesis.studyapp.graphql.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.service.UserService;
import graphql.execution.batched.Batched;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Component
public class GroupResolver implements GraphQLResolver<GroupDTO> {
    @Autowired
    UserService userService;

//LISTÁKRA NEM TUDOM BATChOLNI :(
    public List<UserDTO> users(GroupDTO groupDTO) {

//        BatchLoader<Long, UserDTO> userBatchLoader = new BatchLoader<Long, UserDTO>() {
//            @Override
//            public CompletionStage<List<UserDTO>> load(List<Long> groupIds) {
//                return CompletableFuture.supplyAsync(() -> {
//                    return userService.getUserByGroupIds(groupIds);
//                });
//            }
//        };
//        DataLoader<Long, UserDTO> userLoader = DataLoader.newDataLoader(userBatchLoader);
        System.out.println("GroupResolver: users");
        if(groupDTO.getUsers() == null)
        //return userLoader.load(groupDTO.getId());
            return userService.getUserByGroupId(groupDTO.getId());
        else
            return groupDTO.getUsers();
    }


}
