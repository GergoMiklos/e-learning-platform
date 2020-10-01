package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.service.TestService;
import com.thesis.studyapp.util.SubscriptionUtil;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TestEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TestService testService;

    public Test test(Long testId) {
        return testService.getTest(testId);
    }

    public Test createTest(Long groupId, NameDescInputDto input) {
        return testService.createTest(groupId, input);
    }

    public Test editTest(Long testId, NameDescInputDto input) {
        return testService.editTest(testId, input);
    }

}
