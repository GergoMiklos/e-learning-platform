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

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class UserResolver implements GraphQLResolver<UserDTO> {
    @Autowired
    DataLoaderRegistry dataLoaderRegistry;

    public CompletableFuture<List<GroupDTO>> groups(UserDTO userDTO) {
        if(userDTO.getGroupIds() != null) {
            DataLoader<Long, GroupDTO> grouploader = dataLoaderRegistry.getDataLoader("grouploader");
            //TODO hol sortoljunk? Itt ez jól működik, de livetest-nél (IS!) és liveteststate-nél? (OTT NEM!)
            //todo liveteststate-nél nincs is dataloaderezés, ott mehet db végén order by-al!
            return grouploader.loadMany(userDTO.getGroupIds()).whenCompleteAsync((groups, e) -> groups.sort(new GroupComparator()));
        } else {
            return null;
        }
    }

    private class GroupComparator implements Comparator<GroupDTO> {

        @Override
        public int compare(GroupDTO g1, GroupDTO g2) {
            return g1.getName().compareTo(g2.getName());
        }
    }

    public CompletableFuture<List<GroupDTO>> managedGroups(UserDTO userDTO) {
        if(userDTO.getManagedGroupIds() != null) {
            DataLoader<Long, GroupDTO> grouploader = dataLoaderRegistry.getDataLoader("grouploader");
            return grouploader.loadMany(userDTO.getManagedGroupIds());
        } else {
            return null;
        }
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
