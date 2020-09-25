package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TestTaskInput;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.repository.TaskRepository;
import com.thesis.studyapp.repository.TestRepository;
import com.thesis.studyapp.repository.TestTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestTaskService {

    private final TestTaskRepository testTaskRepository;
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;


    public TestTask getTestTask(Long testTaskId) {
        return getTestTaskById(testTaskId, 1);
    }

    public List<TestTask> getTestTasksByIds(List<Long> ids) {
        return testTaskRepository.findByIdIn(ids, 1);
    }

    //todo ezeknek az isolation leveleknek nézz utána (DE errort dobott a neo4j amikor itt repeatable volt)
    @Transactional
    public List<TestTask> changeTestTaskLevel(List<TestTaskInput> testTaskInputs) {
        testTaskInputs.forEach(TestTaskInput::validate);

        Map<Long, Integer> inputMap = testTaskInputs.stream()
                .collect(Collectors.toMap(TestTaskInput::getId, TestTaskInput::getLevel));

        List<TestTask> testTasks = testTaskRepository.findByIdIn(new ArrayList<>(inputMap.keySet()), 1);
        testTasks.forEach(testTask -> {
            testTask.setLevel(inputMap.get(testTask.getId()));
        });

        return testTaskRepository.save(testTasks, 1);
    }

    @Transactional
    public TestTask addTaskToTest(Long testId, Long taskId, int level) {
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

        return testTaskRepository.save(testTask, 1);
    }

    public List<TestTask> getTestTasksForTest(Long testId) {
        return testTaskRepository.findByTestIdOrderByLevel(testId, 1);
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


}
