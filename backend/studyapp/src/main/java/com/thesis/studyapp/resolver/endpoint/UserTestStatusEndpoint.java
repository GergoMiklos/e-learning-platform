package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.service.UserTestStatusService;
import com.thesis.studyapp.util.StatusSubscriptionUtil;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTestStatusEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver, GraphQLSubscriptionResolver {

    private final StatusSubscriptionUtil statusSubscriptionUtil;
    private final UserTestStatusService userTestStatusService;

    public Publisher<UserTestStatus> testStatusChanges(Long testId) {
        return statusSubscriptionUtil.getPublisher(testId);
    }

    public UserTestStatus studentStatus(Long studentStatusId) {
        return userTestStatusService.getUserTestStatus(studentStatusId);
    }

}
