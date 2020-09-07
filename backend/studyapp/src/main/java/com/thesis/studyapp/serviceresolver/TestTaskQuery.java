package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.TestTaskRepo;
import com.thesis.studyapp.dto.TestTaskDTO;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.TestTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TestTaskQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    TestTaskRepo testTaskRepo;

    public List<TestTaskDTO> getByManyIds(List<Long> ids) {
        return testTaskRepo.getByManyIds(ids);
    }

    public Optional<TestTaskDTO> editTaskLevel(Long testTaskId, int newLevel) {
        TestTask testTask = testTaskRepo.findById(testTaskId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No testtask with id: " + testTaskId));
        testTask.setLevel(newLevel);
        testTaskRepo.save(testTask);
        return testTaskRepo.getById(testTaskId);
    }

}

