package com.thesis.studyapp.service;

import com.thesis.studyapp.model.StudentStatus;

import java.util.List;

public interface StudentStatusService {
    StudentStatus getStudentStatus(Long userTestStatusId);

    List<StudentStatus> getStudentStatusesByIds(List<Long> ids);

    List<StudentStatus> getStudentStatusesForTest(Long testId);

    List<StudentStatus> getStudentStatusesForUser(Long userId);
}
