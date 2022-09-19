package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.model.StudentStatus;
import org.springframework.transaction.annotation.Transactional;

public interface TestSolvingService {
    TaskSolutionDto checkSolution(Long testId, int chosenAnswerNumber);

    StudentStatus calculateNextTask(Long testId);
}
