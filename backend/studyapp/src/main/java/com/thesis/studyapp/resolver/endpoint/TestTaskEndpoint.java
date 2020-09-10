package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.service.TestTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestTaskEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestTaskService testTaskService;

    public TestTaskDto changeTestTaskLevel(Long testTaskId, int newLevel) {
        return testTaskService.changeTestTaskLevel(testTaskId, newLevel);
    }

    public TestTaskDto addTaskToTest(Long testId, Long taskId, int level) {
        return testTaskService.addTaskToTest(testId, taskId, level);
    }

    public void deleteTaskFromTest(Long testTaskId) {
        testTaskService.deleteTaskFromTest(testTaskId);
    }

}

