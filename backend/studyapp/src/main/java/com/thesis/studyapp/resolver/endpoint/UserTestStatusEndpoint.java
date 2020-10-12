package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.thesis.studyapp.dto.TaskSolutionDto;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.service.TestSolvingService;
import com.thesis.studyapp.util.StatusSubscriptionUtil;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTestStatusEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver, GraphQLSubscriptionResolver {

    private final TestSolvingService testSolvingService;
    private final StatusSubscriptionUtil statusSubscriptionUtil;

    public TaskSolutionDto checkSolution(Long userId, Long testId, int chosenAnswerNumber) {
        return testSolvingService.checkSolution(userId, testId, chosenAnswerNumber);
    }

    public TestTask nextTask(Long userId, Long testId) {
        return testSolvingService.getNextTask(userId, testId);
    }

    public Publisher<UserTestStatus> testStatusChanges(Long testId) {
        return statusSubscriptionUtil.getPublisher(testId);
    }

}
