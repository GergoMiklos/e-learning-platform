package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.repository.TaskRepository;
import com.thesis.studyapp.repository.TestRepository;
import com.thesis.studyapp.repository.TestTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestTaskService {

    private final TestTaskRepository testTaskRepository;
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;


    public TestTaskDto getTestTask(Long testTaskId) {
        return TestTaskDto.build(getTestTaskById(testTaskId, 1));
    }

    public List<TestTaskDto> getTestTasksByIds(List<Long> ids) {
        return testTaskRepository.findByIdIn(ids, 1).stream()
                .map(TestTaskDto::build)
                .collect(Collectors.toList());
    }

    public TestTaskDto changeTestTaskLevel(Long testTaskId, int newLevel) {
        TestTask testTask = getTestTaskById(testTaskId, 1);
        testTask.setLevel(newLevel);
        return TestTaskDto.build(testTaskRepository.save(testTask));
    }

    public TestTaskDto addTaskToTest(Long testId, Long taskId, int level) {
        Test test = testRepository.findById(testId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No test with id: " + testId));
        Task task = taskRepository.findById(taskId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No task with id: " + taskId));
        ;
        TestTask testTask = TestTask.builder()
                .level(level)
                .task(task)
                .test(test)
                .build();
        return TestTaskDto.build(testTaskRepository.save(testTask));
    }

    public List<TestTaskDto> getTestTasksForTest(Long testId) {
        return testTaskRepository.findByTestIdOrderByLevel(testId, 1).stream()
                .map(TestTaskDto::build)
                .collect(Collectors.toList());
    }

    public void deleteTaskFromTest(Long testTaskId) {
        testTaskRepository.deleteById(testTaskId);
    }

    private TestTask getTestTaskById(Long testTaskId, int depth) {
        return testTaskRepository.findById(testTaskId, Math.max(depth, 1))
                .orElseThrow(() -> new CustomGraphQLException("No testTask with id: " + testTaskId));
    }


}
