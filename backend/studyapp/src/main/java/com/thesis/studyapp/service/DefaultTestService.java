package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.exception.ForbiddenException;
import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.TestRepository;
import com.thesis.studyapp.util.AuthenticationUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultTestService implements TestService {

    private final TestRepository testRepository;
    private final DefaultGroupService groupService;

    private final AuthenticationUtil authenticationUtil;
    private final DateUtil dateUtil;

    @Override public Test getTest(Long testId) {
        return getTestById(testId, 1);
    }

    @Override public List<Test> getTestsForGroup(Long groupId) {
        return testRepository.findByGroupIdOrderByName(groupId, 1);
    }

    @Override @Transactional
    public Test createTest(Long groupId, NameDescInputDto input) {
        input.validate();
        validateTeacherRole(groupId);
        Group group = groupService.getGroup(groupId);

        Test test = Test.builder()
                .name(input.getName())
                .description(input.getDescription())
                .group(group)
                .build();

        return testRepository.save(test, 1);
    }

    @Override @Transactional
    public Test editTestStatus(Long testId, boolean active) {
        Test test = getTestById(testId, 2);
        validateTeacherRole(test.getGroup().getId());

        if (test.isActive() == active) {
            return test;
        }

        test.setActive(active);
        if (active) {
            test.setStudentStatuses(createStudentStatuses(test.getGroup().getStudents()));
        } else {
            test.setStudentStatuses(deleteStudentStatuses(test.getStudentStatuses()));
        }

        return testRepository.save(test, 2);
    }

    @Override @Transactional
    public Test editTest(Long testId, NameDescInputDto input) {
        input.validate();
        Test test = getTestById(testId, 1);
        validateTeacherRole(test.getGroup().getId());

        test.setName(input.getName());
        test.setDescription(input.getDescription());

        return testRepository.save(test, 1);
    }

    private Set<StudentStatus> createStudentStatuses(Set<User> students) {
        ZonedDateTime creationTime = dateUtil.getCurrentTime();

        return students.stream()
                .map(student -> StudentStatus.builder()
                        .user(student)
                        .active(true)
                        .status(StudentStatus.Status.NOT_STARTED)
                        .statusChangedDate(creationTime)
                        .lastSolutionTime(creationTime)
                        .currentLevel(1)
                        .currentCycle(1)
                        .build()
                )
                .collect(Collectors.toSet());
    }

    private Set<StudentStatus> deleteStudentStatuses(Set<StudentStatus> studentStatuses) {
       studentStatuses.forEach(status -> status.setActive(false));
       return studentStatuses;
    }

    private Test getTestById(Long testId, int depth) {
        return testRepository.findById(testId, depth)
                .orElseThrow(() -> new NotFoundException("No test with id: " + testId));
    }

    private void validateTeacherRole(Long groupId) {
        Long requesterId = authenticationUtil.getPrincipals().getUserId();
        if (!groupService.isTeacherOfGroup(requesterId, groupId)) {
            throw new ForbiddenException("This request authorized only for teachers");
        }
    }


}
