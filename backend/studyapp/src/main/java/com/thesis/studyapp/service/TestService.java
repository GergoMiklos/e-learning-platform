package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final GroupRepository groupRepository;

    public TestDto getTest(Long testId) {
        return convertToDto(getTestById(testId, 0));
    }

    public List<TestDto> getTestsByIds(List<Long> ids) {
        return convertToDto(testRepository.findByIdIn(ids, 0));
    }


    @Transactional
    public TestDto createTest(Long groupId, String name, String description) {
        Group group = groupRepository.findById(groupId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
        Test test = Test.builder()
                .name(name)
                .description(description)
                .group(group)
                .userTestStatuses(new ArrayList<>())
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

    //TODO ez ronda
    @Transactional
    public TestDto changeTestStatus(Long testId, Test.Status status) {
        Test test = getTestById(testId, 2);
        if (!status.equals(test.getStatus())) {
            if (status.equals(Test.Status.ONLINE)) {
                createUserTestStatuses(test);
            } else if (status.equals((Test.Status.OFFLINE))) {
                for (UserTestStatus userTestStatus : test.getUserTestStatuses()) {
                    userTestStatus.setDeprecated(true);
                }
            }
            test.setStatus(status);
            return convertToDto(testRepository.save(test));
        }
        return convertToDto(test);
    }

    public List<TestDto> getTestsForGroup(Long groupId) {
        return convertToDto(testRepository.findByGroupIdOrderByName(groupId, 0));
    }

    //todo ezek menjenek másik service-be?
    private void createUserTestStatuses(Test test) {
        for (User user : test.getGroup().getStudents()) {
            UserTestStatus userTestStatus = UserTestStatus.builder()
                    .user(user)
                    .test(test)
                    .deprecated(false)
                    .build();
            test.getUserTestStatuses().add(userTestStatus);
        }
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
