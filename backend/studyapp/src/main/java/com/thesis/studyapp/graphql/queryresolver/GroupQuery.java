package com.thesis.studyapp.graphql.queryresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.service.GroupService;
import com.thesis.studyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    private GroupService groupService;

    public GroupDTO getGroup(Long id) {
        return groupService.getGroupById(id);
    }

    public List<GroupDTO> getUserGroups(Long id) {
        System.out.println("GroupQueryResolver: getUserGroups");
        return groupService.getGroupsByUserId(id);
    }

    public GroupDTO getGroupDTO(Long id) {
        System.out.println("GroupQueryResolver: getGroupDTO");
        return groupService.getGroupDTObyId(id);
    }

}
