package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.Thread.sleep;

@Component
public class GroupQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private UserRepo userRepo;

    public GroupDTO getGroup(Long id) {
        return groupRepo.getById(id).orElse(null);
    }

    public List<GroupDTO> getByManyGroupIds(List<Long> groupIds) {
        return groupRepo.getByManyIds(groupIds);
    }

    @Transactional
    public Optional<GroupDTO> createGroup(String name, String description) {
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        //Todo
        User user = userRepo.findById(new Long(52)).get();
        group.addAdmin(user);
        group = groupRepo.save(group);
        return groupRepo.getById(group.getId());
    }
}
