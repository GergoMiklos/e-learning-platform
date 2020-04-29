package com.thesis.studyapp.graphql.queryresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.service.GroupService;
import com.thesis.studyapp.service.UserService;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

@Component
public class GroupQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepo groupRepo;


    public GroupDTO getGroup(Long id) {
        return groupRepo.findByGroupId(id).orElse(null);
    }

    public List<GroupDTO> getUserGroups(Long id) {
        System.out.println("GroupQueryResolver: getUserGroups");
        return groupRepo.findByUserId(id);
    }

    public List<GroupDTO> getByManyGroupIds(List<Long> groupIds) {
        return groupRepo.findByManyIds(groupIds);
    }
}
