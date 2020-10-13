package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.exception.ForbiddenException;
import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.TestRepository;
import com.thesis.studyapp.util.AuthenticationUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final AuthenticationUtil authenticationUtil;

    private final DateUtil dateUtil;

    public Test getTest(Long testId) {
        return getTestById(testId, 1);
    }

    public List<Test> getTestsForGroup(Long groupId) {
        return testRepository.findByGroupIdOrderByName(groupId, 1);
    }

    @Transactional
    public Test createTest(Long groupId, NameDescInputDto input) {
        input.validate();
        isTeacherOfTestGroup(groupId);
        Group group = groupService.getGroup(groupId);

        Test test = Test.builder()
                .name(input.getName())
                .description(input.getDescription())
                .group(group)
                .build();

        return testRepository.save(test, 1);
    }

    @Transactional
    public Test editTestStatus(Long testId, boolean active) {
        Test test = getTestById(testId, 2);
        isTeacherOfTestGroup(test.getGroup().getId());

        if (test.isActive() == active) {
            return test;
        }

        test.setActive(active);
        if (active) {
            test.setStudentStatuses(createUserTestStatuses(test.getGroup().getStudents()));
        } else {
            test.setStudentStatuses(new HashSet<>());
        }

        return testRepository.save(test, 2);
    }

    @Transactional
    public Test editTest(Long testId, NameDescInputDto input) {
        input.validate();
        Test test = getTestById(testId, 1);
        isTeacherOfTestGroup(test.getGroup().getId());

        test.setName(input.getName());
        test.setDescription(input.getDescription());

        return testRepository.save(test, 1);
    }

    //todo ez csak akkor működik ha vizsgálod !!! :(
    public Set<StudentStatus> createUserTestStatuses(Set<User> students) {
        ZonedDateTime creationTime = dateUtil.getCurrentTime();

        return students.stream()
                .map(student -> StudentStatus.builder()
                        .user(student)
                        .status(StudentStatus.Status.NOT_STARTED)
                        .statusChangedDate(creationTime)
                        .currentLevel(1)
                        .currentCycle(1)
                        .build()
                )
                .collect(Collectors.toSet());
    }

    private Test getTestById(Long testId, int depth) {
        return testRepository.findById(testId, depth)
                .orElseThrow(() -> new NotFoundException("No test with id: " + testId));
    }

    private void isTeacherOfTestGroup(Long groupId) {
        Long requesterId = authenticationUtil.getPrincipals().getUserId();
        if (!groupService.isTeacherOfGroup(requesterId, groupId)) {
            throw new ForbiddenException("This request authorized only for teachers");
        }

    }


}
