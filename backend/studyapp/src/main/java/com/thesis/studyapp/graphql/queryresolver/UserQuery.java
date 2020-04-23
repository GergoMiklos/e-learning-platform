package com.thesis.studyapp.graphql.queryresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.service.GroupService;
import com.thesis.studyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    private UserService userService;

    public UserDTO getUser(Long id) {
        return userService.getUserById(id);
    }

}
