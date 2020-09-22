package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.service.UserTestStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTestStatusEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final UserTestStatusService userTestStatusService;

    public TaskSolutionDto checkSolution(Long userId, Long testId, int chosenAnswerNumber) {
        return userTestStatusService.checkSolution(userId, testId, chosenAnswerNumber);
    }

    public TestTaskDto nextTask(Long userId, Long testId) {
        return userTestStatusService.getNextTask(userId, testId);
    }

}
