package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TestTaskInputDto;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.security.annotation.Authenticated;
import com.thesis.studyapp.service.DefaultTestTaskService;
import com.thesis.studyapp.service.TestTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestTaskEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestTaskService testTaskService;

    @Authenticated
    public List<TestTask> editTestTasks(Long testId, List<TestTaskInputDto> testTaskInputDtos) {
        return testTaskService.editTestTasks(testId, testTaskInputDtos);
    }

    @Authenticated
    public TestTask addTaskToTest(Long testId, Long taskId, int level) {
        return testTaskService.addTaskToTest(testId, taskId, level);
    }

    @Authenticated
    public void deleteTaskFromTest(Long testTaskId) {
        testTaskService.deleteTaskFromTest(testTaskId);
    }

}

