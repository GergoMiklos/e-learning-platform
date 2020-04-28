package com.thesis.studyapp.graphql.queryresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
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
    private DataLoaderRegistry dataLoaderRegistry;

    private DataLoaderDispatcherInstrumentation instrumentation;

    public GroupDTO getGroup(Long id) {
        return groupService.getGroupById(id);
    }

    public List<GroupDTO> getUserGroups(Long id) throws InterruptedException {
        //dataLoaderRegistry.dispatchAll();
        System.out.println("GroupQueryResolver: getUserGroups");
        List<GroupDTO> result = groupService.getGroupsByUserId(id);
//        System.out.println("Dispatch now!");
//        dataLoaderRegistry.dispatchAll();
//        System.out.println("Dispatch finished for:");
//        for(String s : dataLoaderRegistry.getKeys()) {
//            System.out.println("  " + s);
//            dataLoaderRegistry.getDataLoader(s).dispatchAndJoin();
//        }
//        DataLoader<Object, Object> userloader = dataLoaderRegistry.getDataLoader("userloader");
        //userloader.dispatchAndJoin();
//        System.out.println("GroupQuery result:");
//        for(GroupDTO g : result) {
//            System.out.println("result id: " + g.getId());
//            System.out.println("result userids: " + g.getUserIds());
//            System.out.println("result users: " + g.getUsers());
//        }

        System.out.println("result back!");
        return result;
    }

    public GroupDTO getGroupDTO(Long id) {
        System.out.println("GroupQueryResolver: getGroupDTO");
        GroupDTO result = groupService.getGroupDTObyId(id);

        dataLoaderRegistry.dispatchAll();
        return result;

    }

}
