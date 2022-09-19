package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.service.DefaultStudentStatusService;
import com.thesis.studyapp.service.StudentStatusService;
import com.thesis.studyapp.util.StatusSubscriptionUtil;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentStatusEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver, GraphQLSubscriptionResolver {

    private final StatusSubscriptionUtil statusSubscriptionUtil;
    private final StudentStatusService studentStatusService;

    public Publisher<StudentStatus> testStatusChangesByTest(Long testId) {
        return statusSubscriptionUtil.getPublisherByTest(testId);
    }

    public Publisher<StudentStatus> testStatusChangesByUser(Long userId) {
        return statusSubscriptionUtil.getPublisherByUser(userId);
    }

    public StudentStatus studentStatus(Long studentStatusId) {
        return studentStatusService.getStudentStatus(studentStatusId);
    }

}
