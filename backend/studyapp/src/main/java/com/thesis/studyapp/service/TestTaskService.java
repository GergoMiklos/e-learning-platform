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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestTaskService {

    private final TestTaskRepository testTaskRepository;
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;


    public TestTaskDto getTestTask(Long testTaskId) {
        return convertToDto(getTestTaskById(testTaskId, 1));
    }

    public List<TestTaskDto> getTestTasksByIds(List<Long> ids) {
        return convertToDto(testTaskRepository.findByIdIn(ids, 1));
    }

    //todo ezeknek az isolation leveleknek nézz utána
    @Transactional
    public TestTaskDto changeTestTaskLevel(Long testTaskId, int newLevel) {
        TestTask testTask = getTestTaskById(testTaskId, 1);
        testTask.setLevel(newLevel);
        return convertToDto(testTaskRepository.save(testTask, 1));
    }

    @Transactional
    public TestTaskDto addTaskToTest(Long testId, Long taskId, int level) {
        Test test = testRepository.findById(testId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No test with id: " + testId));
        Task task = taskRepository.findById(taskId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No task with id: " + taskId));
        task.setUsage(task.getUsage() + 1);
        TestTask testTask = TestTask.builder()
                .level(level)
                .task(task)
                .test(test)
                .build();
        return convertToDto(testTaskRepository.save(testTask, 1));
    }

    public List<TestTaskDto> getTestTasksForTest(Long testId) {
        return convertToDto(testTaskRepository.findByTestIdOrderByLevel(testId, 1));
    }

    public void deleteTaskFromTest(Long testTaskId) {
        testTaskRepository.deleteById(testTaskId);
    }

    //Todo, minden Query típusú függvény Optional-lal térjen vissza (GraphQL kezeli),
    // és ha get->null-nál ne dobjunk kivételt,
    // csak Mutation-nél, amikor már tényleg probléma van
    private TestTask getTestTaskById(Long testTaskId, int depth) {
        return testTaskRepository.findById(testTaskId, Math.max(depth, 1))
                .orElseThrow(() -> new CustomGraphQLException("No testTask with id: " + testTaskId));
    }

    private TestTaskDto convertToDto(TestTask testTask) {
        return TestTaskDto.build(testTask);
    }

    private List<TestTaskDto> convertToDto(List<TestTask> testTasks) {
        return testTasks.stream()
                .map(TestTaskDto::build)
                .collect(Collectors.toList());
    }


}
