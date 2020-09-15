package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.service.UserTestStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTestStatusEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final UserTestStatusService userTestStatusService;


    //TODO visszatérést kiegészíthetet plusz infókkal (pl hanyadik kérdés, hány százalékos megoldosttság...)
    public int checkSolution(Long userId, Long testId, Long chosenAnswerNumber) {
        return userTestStatusService.checkSolution(userId, testId, chosenAnswerNumber);
    }

    //todo ezt hova? kilehetne egészíteni plusz infókkal?
    public TaskDto task(Long userId, Long testId) {
        return userTestStatusService.getNextTask(userId, testId);
    }

}
