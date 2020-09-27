package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.util.DataLoaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TestResolver implements GraphQLResolver<Test> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<List<TestTask>> testTasks(Test test) {
        return dataLoaderUtil.loadData(test.getTestTasks(), DataLoaderUtil.TESTTASK_LOADER)
                .thenApplyAsync((testTasks) -> {
                    testTasks.sort(new TestTask.TestTaskComparator());
                    return testTasks;
                });
    }

    public CompletableFuture<List<UserTestStatus>> userTestStatuses(Test test) {
        return dataLoaderUtil.loadData(test.getUserTestStatuses(), DataLoaderUtil.USERTESTSTATUS_LOADER)
                .thenApplyAsync((userTestStatuses) -> {
                    userTestStatuses.sort(new UserTestStatus.UserTestStatusComparator());
                    return userTestStatuses;
                });
    }

    public CompletableFuture<Group> group(Test test) {
        return dataLoaderUtil.loadData(test.getGroup(), DataLoaderUtil.GROUP_LOADER);
    }

    //todo szólni kéne ha null?
    public CompletableFuture<Integer> allTasks(Test test) {
        return CompletableFuture.completedFuture(test.getTestTasks() == null ? 0 : test.getTestTasks().size());
    }
}
