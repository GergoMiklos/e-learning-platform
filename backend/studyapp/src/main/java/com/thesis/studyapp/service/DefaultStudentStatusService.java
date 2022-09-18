package com.thesis.studyapp.service;

import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.repository.StudentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class DefaultStudentStatusService implements StudentStatusService {

    private final StudentStatusRepository studentStatusRepository;

    @Override public StudentStatus getStudentStatus(Long userTestStatusId) {
        return getStudentStatusById(userTestStatusId, 1);
    }

    @Override public List<StudentStatus> getStudentStatusesByIds(List<Long> ids) {
        return studentStatusRepository.findByIdIn(ids, 1);
    }

    @Override public List<StudentStatus> getStudentStatusesForTest(Long testId) {
        return studentStatusRepository.findByActiveTrueAndTestIdOrderByUserName(testId, 1);
    }

    @Override public List<StudentStatus> getStudentStatusesForUser(Long userId) {
        return studentStatusRepository.findByActiveTrueAndUserIdOrderByTestName(userId, 1);
    }

    private StudentStatus getStudentStatusById(Long userTestStatusId, int depth) {
        return studentStatusRepository.findById(userTestStatusId, max(1, depth))
                .orElseThrow(() -> new NotFoundException("No StudentStatus with id: " + userTestStatusId));
    }

}
