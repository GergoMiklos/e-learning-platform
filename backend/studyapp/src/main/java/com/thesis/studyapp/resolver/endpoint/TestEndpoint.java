package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.dto.TestDto;
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

    public TestDto createTest(Long groupId, NameDescInputDto input) {
        return testService.createTest(groupId, input);
    }

    public TestDto editTest(Long testId, NameDescInputDto input) {
        return testService.editTest(testId, input);
    }

}
