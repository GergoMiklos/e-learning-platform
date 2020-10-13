package com.thesis.studyapp.service;

import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.repository.StudentStatusRepository;
import com.thesis.studyapp.repository.StudentTaskStatusRepository;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class StudentStatusService {

    private final StudentStatusRepository studentStatusRepository;
    private final StudentTaskStatusRepository studentTaskStatusRepository;

    private final DateUtil dateUtil;
    private final ApplicationEventPublisher eventPublisher;

    public StudentStatus getUserTestStatus(Long userTestStatusId) {
        return getUserTestStatusById(userTestStatusId, 1);
    }

    public List<StudentStatus> getUserTestStatusesByIds(List<Long> ids) {
        return studentStatusRepository.findByIdIn(ids, 1);
    }

    //todo (FONTOS: objectresolvernél ha errort dobunk egy fieldnél, attól még a többi meglehet (külön folyamat/request))
    public List<StudentStatus> getUserTestStatusesForTest(Long testId) {
        return studentStatusRepository.findByTestIdOrderByUserName(testId, 1);
    }

    public List<StudentStatus> getUserTestStatusesForUser(Long userId) {
        return studentStatusRepository.findByUserIdOrderByTestName(userId, 1);
    }

    private StudentStatus getUserTestStatusById(Long userTestStatusId, int depth) {
        return studentStatusRepository.findById(userTestStatusId, max(1, depth))
                .orElseThrow(() -> new NotFoundException("No StudentStatus with id: " + userTestStatusId));
    }

}
