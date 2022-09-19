package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.security.annotation.Authenticated;
import com.thesis.studyapp.service.DefaultTestService;
import com.thesis.studyapp.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TestEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestService testService;

    public Test test(Long testId) {
        return testService.getTest(testId);
    }

    @Authenticated
    public Test createTest(Long groupId, NameDescInputDto input) {
        return testService.createTest(groupId, input);
    }

    @Authenticated
    public Test editTest(Long testId, NameDescInputDto input) {
        return testService.editTest(testId, input);
    }

    @Authenticated
    public Test editTestStatus(Long testId, boolean active) {
        return testService.editTestStatus(testId, active);
    }

}
