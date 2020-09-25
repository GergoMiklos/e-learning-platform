package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TestTaskInput;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.service.TestTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestTaskEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestTaskService testTaskService;

    public List<TestTask> changeTestTaskLevel(List<TestTaskInput> testTaskInputs) {
        return testTaskService.changeTestTaskLevel(testTaskInputs);
    }

    public TestTask addTaskToTest(Long testId, Long taskId, int level) {
        return testTaskService.addTaskToTest(testId, taskId, level);
    }

    public void deleteTaskFromTest(Long testTaskId) {
        testTaskService.deleteTaskFromTest(testTaskId);
    }

}

