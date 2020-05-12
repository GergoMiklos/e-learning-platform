package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.LiveTestDTO;
import com.thesis.studyapp.dto.NewsDTO;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.serviceresolver.UserQuery;
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
    DataLoaderRegistry dataLoaderRegistry;

    @Autowired
    UserQuery userQuery;


    public CompletableFuture<List<UserDTO>> users(GroupDTO groupDTO) {
//        if(groupDTO.getUserIds() != null) {
//            DataLoader<Long, UserDTO> userloader = dataLoaderRegistry.getDataLoader("userloader");
//            return userloader.loadMany(groupDTO.getUserIds());
//        } else {
//            return null;
//        }
        return CompletableFuture.supplyAsync(() -> userQuery.getByGroupId(groupDTO.getId()));
    }

    public CompletableFuture<List<UserDTO>> admins(GroupDTO groupDTO) {
        if(groupDTO.getAdminIds() != null) {
            DataLoader<Long, UserDTO> userloader = dataLoaderRegistry.getDataLoader("userloader");
            return userloader.loadMany(groupDTO.getAdminIds());
        } else {
            return null;
        }
    }

    public CompletableFuture<NewsDTO> news(GroupDTO groupDTO) {
        if(groupDTO.getNewsId() != null) {
            DataLoader<Long, NewsDTO> newsloader = dataLoaderRegistry.getDataLoader("newsloader");
            return newsloader.load(groupDTO.getNewsId());
        } else {
            return null;
        }
    }

    public CompletableFuture<List<LiveTestDTO>> liveTests(GroupDTO groupDTO) {
        if(groupDTO.getLiveTestIds() != null) {
            DataLoader<Long, LiveTestDTO> livetestloader = dataLoaderRegistry.getDataLoader("livetestloader");
            return livetestloader.loadMany(groupDTO.getLiveTestIds());
        } else {
            return null;
        }
    }

}
