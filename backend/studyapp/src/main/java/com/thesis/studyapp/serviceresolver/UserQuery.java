package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    private UserRepo userRepo;

    public Optional<UserDTO> user(Long id) {
        return userRepo.getById(id);
    }

    public List<UserDTO> getManyByIds(List<Long> ids) {
        return userRepo.getByManyIds(ids);
    }

    public List<UserDTO> getByGroupId(Long groupId) {
        return userRepo.getByGroupId(groupId);
    }



}
