package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.repository.TestTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TestTaskEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestTaskRepository testTaskRepository;

    public List<TestTaskDto> getTestTasksByIds(List<Long> ids) {
        return testTaskRepository.getByIds(ids);
    }

    public Optional<TestTaskDto> editTaskLevel(Long testTaskId, int newLevel) {
        TestTask testTask = testTaskRepository.findById(testTaskId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No testtask with id: " + testTaskId));
        testTask.setLevel(newLevel);
        testTaskRepository.save(testTask);
        return testTaskRepository.getById(testTaskId);
    }

}

