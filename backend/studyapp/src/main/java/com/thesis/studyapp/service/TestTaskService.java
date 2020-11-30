package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TestTaskInputDto;
import com.thesis.studyapp.model.TestTask;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TestTaskService {
    TestTask getTestTask(Long testTaskId);

    List<TestTask> getTestTasksForTest(Long testId);

    List<TestTask> editTestTasks(Long testId, List<TestTaskInputDto> testTaskInputDtos);

    TestTask addTaskToTest(Long testId, Long taskId, int level);

    void deleteTaskFromTest(Long testTaskId);
}
