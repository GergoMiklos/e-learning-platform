package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.repository.UserTestStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserTestStatusEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final UserTestStatusRepository userTestStatusRepository;

    public List<UserTestStatusDto> getUserTestStatusesByIds(List<Long> lTSIds) {
        return userTestStatusRepository.getByManyIds(lTSIds);
    }

    public List<UserTestStatusDto> getByTestId(Long id) {
        return userTestStatusRepository.getByTestId(id);
    }
}
