package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestService testService;

    public TestDto test(Long testId) {
        return testService.getTest(testId);
    }

    public TestDto createTest(Long groupId, String name, String description) {
        return testService.createTest(groupId, name, description);
    }

    public TestDto editTest(Long testId, String name, String description) {
        return testService.editTest(testId, name, description);

    }

    public TestDto changeTestStatus(Long testId, String status) {
        return testService.changeTestStatus(testId, Test.Status.ONLINE); //Todo
    }


}
