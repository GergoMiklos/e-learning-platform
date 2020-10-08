package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.TestRepository;
import com.thesis.studyapp.util.DateUtil;
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

    public Test getTest(Long testId) {
        return getTestById(testId, 1);
    }

    public List<Test> getTestsByIds(List<Long> ids) {
        return testRepository.findByIdIn(ids, 1);
    }

    //TODO first task? Ha valaki később csatlakozik?
    @Transactional
    public Test createTest(Long groupId, NameDescInputDto input) {
        input.validate();
        Group group = groupRepository.findById(groupId, 1)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
        Test test = Test.builder()
                .name(input.getName())
                .description(input.getDescription())
                .group(group)
                .userTestStatuses(createUserTestStatuses(group.getStudents()))
                .build();
        return testRepository.save(test, 2);
    }

    @Transactional
    public Test editTest(Long testId, NameDescInputDto input) {
        input.validate();
        Test test = getTestById(testId, 1);
        test.setName(input.getName());
        test.setDescription(input.getDescription());
        return testRepository.save(test, 1);
    }

    public List<Test> getTestsForGroup(Long groupId) {
        return testRepository.findByGroupIdOrderByName(groupId, 1);
    }

    //todo ez csak akkor működik ha vizsgálod !!! :(
    //todo saveljen is? Úgy ezt lehet használni group serviceből is
    public Set<UserTestStatus> createUserTestStatuses(Set<User> students) {
        return students.stream()
                .map(student -> UserTestStatus.builder()
                        .user(student)
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


}
