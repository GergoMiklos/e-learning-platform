package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final UserRepository userRepository;

    public Optional<UserDto> getUser(Long id) {
        return userRepository.getById(id);
    }

    public List<UserDto> getUsersByIds(List<Long> ids) {
        return userRepository.getByIds(ids);
    }


}
