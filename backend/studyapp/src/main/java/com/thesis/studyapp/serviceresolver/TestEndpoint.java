package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.TaskRepository;
import com.thesis.studyapp.repository.TestRepository;
import com.thesis.studyapp.repository.TestTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TestEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestRepository testRepository;
    private final TestTaskRepository testTaskRepository;
    private final TaskRepository taskRepository;
    private final GroupRepository groupRepository;

    public List<TestDto> getTestsByIds(List<Long> testIds) {
        return testRepository.getByIds(testIds);
    }

    public Optional<TestDto> getTest(Long testId) {
        return testRepository.getById(testId);
    }

    @Transactional
    public Optional<TestDto> createTest(Long groupId, String name, String description) {
        Group group = groupRepository.findById(groupId, 0).orElseThrow(() -> new RuntimeException("No group with id: " + groupId));
        Test test = new Test();
        test.setName(name);
        test.setDescription(description);
        test.setGroup(group);
        test = testRepository.save(test);
        return testRepository.getById(test.getId());
    }

    @Transactional
    public Optional<TestDto> editTest(Long id, String name, String description) {
        Test test = testRepository.findById(id, 0)
                .orElseThrow(() -> new CustomGraphQLException("No test with id: " + id));
        test.setName(name);
        test.setDescription(description);
        test = testRepository.save(test);
        return testRepository.getById(test.getId());
    }

    //TODO, ha testaskkal tér vissza, akkor menjen abba a service-be
    public Optional<TestTaskDto> addTaskToTest(Long testId, Long taskId, int level) {
        Test test = testRepository.findById(testId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No test with id: " + testId));
        Task task = taskRepository.findById(taskId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No task with id: " + taskId));
        TestTask testTask = new TestTask();
        testTask.setTask(task);
        testTask.setLevel(level);
        test.addTask(testTask);
        testRepository.save(test);
        return testTaskRepository.getById(testTask.getId());
    }

    public void deleteTaskFromTest(Long testTaskId) {
        testTaskRepository.deleteById(testTaskId);
    }

    @Transactional
    public void setTestStatus(Long testId, Test.Status status) {
        Test test = testRepository.findById(testId, 2)
                .orElseThrow(() -> new CustomGraphQLException("No test with id: " + testId));
        if (!status.equals(test.getStatus())) {
            if (status.equals(Test.Status.ONLINE)) {
                createUserTestStatuses(test);
            } else if (status.equals((Test.Status.OFFLINE))) {
                for (UserTestStatus userTestStatus : test.getUserTestStatuses()) {
                    userTestStatus.setDeprecated(true);
                }
            }
            test.setStatus(status);
            testRepository.save(test);
        }
    }

    private void createUserTestStatuses(Test test) {
        for (User user : test.getGroup().getStudents()) {
            UserTestStatus userTestStatus = UserTestStatus.builder()
                    .user(user)
                    .deprecated(false)
                    .build();
            test.getUserTestStatuses().add(userTestStatus);
        }
    }

}
