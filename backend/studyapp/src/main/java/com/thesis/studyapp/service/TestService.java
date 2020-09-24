package com.thesis.studyapp.service;

import com.thesis.studyapp.configuration.DateUtil;
import com.thesis.studyapp.dto.NameDescInput;
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
        return convertToDto(getTestById(testId, 1));
    }

    public List<TestDto> getTestsByIds(List<Long> ids) {
        return convertToDto(testRepository.findByIdIn(ids, 0));
    }


    //TODO first task? Ha valaki később csatlakozik?
    @Transactional
    public TestDto createTest(Long groupId, NameDescInput input) {
        input.validate();
        Group group = groupRepository.findById(groupId, 1)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
        Test test = Test.builder()
                .name(input.getName())
                .description(input.getDescription())
                .group(group)
                .userTestStatuses(createUserTestStatuses(group.getStudents()))
                .build();
        return convertToDto(testRepository.save(test, 1));
    }

    @Transactional
    public TestDto editTest(Long testId, NameDescInput input) {
        input.validate();
        Test test = getTestById(testId, 0);
        test.setName(input.getName());
        test.setDescription(input.getDescription());
        return convertToDto(testRepository.save(test, 1));
    }

    public List<TestDto> getTestsForGroup(Long groupId) {
        return convertToDto(testRepository.findByGroupIdOrderByName(groupId, 1));
    }

    //todo ez csak akkor működik ha vizsgálod !!! :(
    private Set<UserTestStatus> createUserTestStatuses(Set<User> users) {
        return users.stream()
                .map(user -> UserTestStatus.builder()
                        .user(user)
                        .status(UserTestStatus.Status.NOT_STARTED)
                        .statusChangedDate(dateUtil.getCurrentTime())
                        .currentLevel(1)
                        .currentCycle(1)
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
