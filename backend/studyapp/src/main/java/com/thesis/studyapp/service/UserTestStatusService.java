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
        return convertToDto(getUserTestStatusById(userTestStatusId, 1));
    }

    public List<UserTestStatusDto> getUserTestStatusesByIds(List<Long> ids) {
        return convertToDto(userTestStatusRepository.findByIdIn(ids, 1));
    }

    //TODO ilyenkor testId check? pl get Test with depth 1, filter userId == userId && deprecated == false
    // vagy ez ne is legyen itt mert csak objectresolverből hívjuk?
    // (FONTOS: objectresolvernél ha errort dobunk egy filednél, attól még a többi meglehet (külön folyamat))
    public List<UserTestStatusDto> getUserTestStatusesForTest(Long testId) {
        return convertToDto(userTestStatusRepository.findByDeprecatedIsFalseAndTestIdIs(testId, 1));
    }

    public List<UserTestStatusDto> getUserTestStatusesForUser(Long userId) {
        return convertToDto(userTestStatusRepository.findByDeprecatedIsFalseAndUserIdIs(userId, 1));
    }

    private UserTestStatus getUserTestStatusById(Long userTestStatusId, int depth) {
        return userTestStatusRepository.findById(userTestStatusId, Math.max(1, depth))
                .orElseThrow(() -> new CustomGraphQLException("No UserTestStatus with id: " + userTestStatusId));
    }

    private UserTestStatusDto convertToDto(UserTestStatus userTestStatus) {
        return UserTestStatusDto.build(userTestStatus);
    }

    private List<UserTestStatusDto> convertToDto(List<UserTestStatus> userTestStatuses) {
        return userTestStatuses.stream()
                .map(UserTestStatusDto::build)
                .collect(Collectors.toList());
    }

}
