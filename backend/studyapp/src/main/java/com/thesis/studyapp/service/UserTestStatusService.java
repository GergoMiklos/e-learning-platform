package com.thesis.studyapp.service;


import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.UserTestStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserTestStatusService {

    private final UserTestStatusRepository userTestStatusRepository;

    public UserTestStatusDto getUserTestStatus(Long userTestStatusId) {
        return UserTestStatusDto.build(getUserTestStatusById(userTestStatusId, 1));
    }

    public List<UserTestStatusDto> getUserTestStatusesByIds(List<Long> ids) {
        return userTestStatusRepository.findByIdIn(ids, 1).stream()
                .map(UserTestStatusDto::build)
                .collect(Collectors.toList());
    }

    //TODO ilyenkor testId check? pl get Test with depth 1, filter userId == userId && deprecated == false
    // vagy ez ne is legyen itt mert csak objectresolverből hívjuk??
    // Obejctresolver repot vagy servicet használjon
    public List<UserTestStatusDto> getUserTestStatusesForTest(Long testId) {
        return userTestStatusRepository.findByDeprecatedIsFalseAndTestIdIs(testId, 1).stream()
                .map(UserTestStatusDto::build)
                .collect(Collectors.toList());
    }

    public List<UserTestStatusDto> getUserTestStatusesForUser(Long userId) {
        return userTestStatusRepository.findByDeprecatedIsFalseAndUserIdIs(userId, 1).stream()
                .map(UserTestStatusDto::build)
                .collect(Collectors.toList());
    }

    private UserTestStatus getUserTestStatusById(Long userTestStatusId, int depth) {
        return userTestStatusRepository.findById(userTestStatusId, Math.max(1, depth))
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus with id: " + userTestStatusId));
    }


}
