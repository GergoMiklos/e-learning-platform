package com.thesis.studyapp.graphql.queryresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.service.GroupService;
import com.thesis.studyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    private UserRepo userRepo;

    public UserDTO getUserById(Long id) {
        return userRepo.findByUserId(id);
    }

    public List<UserDTO> getManyByIds(List<Long> ids) {
        return userRepo.findByManyIds(ids);
    }

}
