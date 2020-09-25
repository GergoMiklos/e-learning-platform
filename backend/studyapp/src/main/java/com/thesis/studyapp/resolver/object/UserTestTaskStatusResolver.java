package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.UserTestTaskStatus;
import com.thesis.studyapp.util.DataLoaderUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserTestTaskStatusResolver implements GraphQLResolver<UserTestTaskStatus> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<TestTask> testTask(UserTestTaskStatus userTestTaskStatus) {
        return dataLoaderUtil.loadData(userTestTaskStatus.getTestTask(), DataLoaderUtil.TESTTASK_LOADER);
    }

    public CompletableFuture<String> lastSolutionTime(UserTestTaskStatus userTestTaskStatus) {
        return CompletableFuture.completedFuture(DateUtil.convertToIsoString(userTestTaskStatus.getLastSolutionTime()));
    }
}
