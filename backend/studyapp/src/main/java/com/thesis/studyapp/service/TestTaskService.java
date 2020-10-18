package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TestTaskInputDto;
import com.thesis.studyapp.exception.ForbiddenException;
import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.repository.TestTaskRepository;
import com.thesis.studyapp.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestTaskService {

    private final TestTaskRepository testTaskRepository;

    private final GroupService groupService;
    private final TaskService taskService;
    private final TestService testService;

    private final AuthenticationUtil authenticationUtil;

    public TestTask getTestTask(Long testTaskId) {
        return getTestTaskById(testTaskId, 1);
    }

    public List<TestTask> getTestTasksForTest(Long testId) {
        return testTaskRepository.findByTestIdOrderByLevel(testId, 1);
    }

    @Transactional
    public List<TestTask> editTestTasks(Long testId, List<TestTaskInputDto> testTaskInputDtos) {
        testTaskInputDtos.forEach(TestTaskInputDto::validate);
        Test test = testService.getTest(testId);
        isTeacherOfTestGroup(test.getGroup().getId());

        List<TestTask> testTasks = testTaskRepository.findByIdIn(testTaskInputDtos.stream()
                .map(TestTaskInputDto::getId)
                .collect(Collectors.toList()), 1);

        testTaskInputDtos.forEach(changes -> {
            testTasks.stream().
                    filter(testTask -> testTask.getId().equals(changes.getId()))
                    .findFirst()
                    .ifPresent(testTask -> {
                        if (changes.getLevel() != null) {
                            testTask.setLevel(changes.getLevel());
                        }
                        if (changes.getExplanation() != null) {
                            testTask.setExplanation(changes.getExplanation());
                        }
                    });
        });

        return testTaskRepository.save(testTasks, 1);
    }

    @Transactional
    public TestTask addTaskToTest(Long testId, Long taskId, int level) {
        Test test = testService.getTest(testId);
        Task task = taskService.getTask(taskId);
        isTeacherOfTestGroup(test.getGroup().getId());

        TestTask testTask = TestTask.builder()
                .level(level)
                .task(task)
                .test(test)
                .build();

        taskService.setTaskUsage(testTask.getTask().getId(), false);

        return testTaskRepository.save(testTask, 1);
    }

    @Transactional
    public void deleteTaskFromTest(Long testTaskId) {
        TestTask testTask = getTestTaskById(testTaskId, 2);
        isTeacherOfTestGroup(testTask.getTest().getGroup().getId());

        testTaskRepository.deleteFromTest(testTaskId);

        taskService.setTaskUsage(testTask.getTask().getId(), false);
    }

    private TestTask getTestTaskById(Long testTaskId, int depth) {
        return testTaskRepository.findById(testTaskId, Math.max(depth, 1))
                .orElseThrow(() -> new NotFoundException("No testTask with id: " + testTaskId));
    }

    private void isTeacherOfTestGroup(Long groupId) {
        Long requesterId = authenticationUtil.getPrincipals().getUserId();
        if (!groupService.isTeacherOfGroup(requesterId, groupId)) {
            throw new ForbiddenException("This request authorized only for teachers");
        }
    }


}
