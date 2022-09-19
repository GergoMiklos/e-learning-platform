package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.security.annotation.Authenticated;
import com.thesis.studyapp.service.DefaultTestSolvingService;
import com.thesis.studyapp.service.TestSolvingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestSolvingEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestSolvingService testSolvingService;

    @Authenticated
    public TaskSolutionDto checkSolution(Long testId, int chosenAnswerNumber) {
        return testSolvingService.checkSolution(testId, chosenAnswerNumber);
    }

    @Authenticated
    public StudentStatus calculateNextTask(Long testId) {
        return testSolvingService.calculateNextTask(testId);
    }
}
