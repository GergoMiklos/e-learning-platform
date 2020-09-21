package com.thesis.studyapp.service;

import com.thesis.studyapp.configuration.DateUtil;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final GroupRepository groupRepository;

    private final DateUtil dateUtil;

    public TestDto getTest(Long testId) {
        return convertToDto(getTestById(testId, 0));
    }

    public List<TestDto> getTestsByIds(List<Long> ids) {
        return convertToDto(testRepository.findByIdIn(ids, 0));
    }


    //TODO first task? ha valaki később csatlakozik?
    @Transactional
    public TestDto createTest(Long groupId, String name, String description) {
        Group group = groupRepository.findById(groupId, 1)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
        Test test = Test.builder()
                .name(name)
                .description(description)
                .group(group)
                .userTestStatuses(createUserTestStatuses(group.getStudents()))
                .build();
        return convertToDto(testRepository.save(test));
    }

    @Transactional
    public TestDto editTest(Long testId, String name, String description) {
        Test test = getTestById(testId, 0);
        test.setName(name);
        test.setDescription(description);
        return convertToDto(testRepository.save(test));
    }

    public List<TestDto> getTestsForGroup(Long groupId) {
        return convertToDto(testRepository.findByGroupIdOrderByName(groupId, 0));
    }

    //todo ez csak akkor működik ha vizsgálod !!! :(
    private Set<UserTestStatus> createUserTestStatuses(Set<User> users) {
        return users.stream()
                .map(user -> UserTestStatus.builder()
                        .user(user)
                        .status(UserTestStatus.Status.NOT_STARTED)
                        .statusChangedDate(dateUtil.getCurrentTime())
                        .build()
                )
                .collect(Collectors.toSet());
    }

    public Test getTestById(Long testId, int depth) {
        return testRepository.findById(testId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No test with id: " + testId));
    }

    private TestDto convertToDto(Test test) {
        return TestDto.build(test);
    }

    private List<TestDto> convertToDto(List<Test> tests) {
        return tests.stream()
                .map(TestDto::build)
                .collect(Collectors.toList());
    }

}
