package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.TaskRepo;
import com.thesis.studyapp.dao.TestRepo;
import com.thesis.studyapp.dao.TestTaskRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.dto.TestTaskDTO;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class TestQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    TestRepo testRepo;

    @Autowired
    TestTaskRepo testTaskRepo;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    public List<TestDTO> getByManyTestIds(List<Long> testIds) {
        return testRepo.getByManyIds(testIds);
    }

    public Optional<TestDTO> test(Long testId) {
        return testRepo.getById(testId);
    }

    @Transactional
    public Optional<TestDTO> createTest(Long userId, String name, String description) {
        User user = userRepo.findById(userId, 0).orElseThrow(()->new RuntimeException("No user with id: " + userId));
        Test test = new Test();
        test.setName(name);
        test.setDescription(description);
        test.setOwner(user);
        test = testRepo.save(test);
        return testRepo.getById(test.getId());
    }

    @Transactional
    public Optional<TestDTO> editTest(Long id, String name, String description) {
        Test test = testRepo.findById(id, 0).orElseThrow(()-> new CustomGraphQLException("No test with id: " + id));
        test.setName(name);
        test.setDescription(description);
        test = testRepo.save(test);
        return testRepo.getById(test.getId());
    }

    public Optional<TestTaskDTO> addTaskToTest(Long testId, Long taskId, int level) {
        Test test = testRepo.findById(testId, 0).orElseThrow(()-> new CustomGraphQLException("No test with id: " + testId));
        Task task = taskRepo.findById(taskId, 0).orElseThrow(()-> new CustomGraphQLException("No task with id: " + taskId));
        TestTask testTask = new TestTask();
        testTask.setTask(task);
        testTask.setLevel(level);
        test.addTask(testTask);
        testRepo.save(test);
        return testTaskRepo.getById(testTask.getId());
    }

    public void deleteTaskFromTest(Long testTaskId) {
        testTaskRepo.deleteById(testTaskId);
    }


}
