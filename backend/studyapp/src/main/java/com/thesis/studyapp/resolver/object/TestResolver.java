package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.util.DataLoaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.thesis.studyapp.util.ComparatorUtil.getTestTaskComparator;
import static com.thesis.studyapp.util.ComparatorUtil.getUserTestStatusComparator;

@Component
@RequiredArgsConstructor
public class TestResolver implements GraphQLResolver<Test> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<List<TestTask>> testTasks(Test test) {
        return dataLoaderUtil.loadData(test.getTestTasks(), DataLoaderUtil.TESTTASK_LOADER)
                .thenApplyAsync((testTasks) -> {
                    testTasks.sort(getTestTaskComparator());
                    return testTasks;
                });
    }

    public CompletableFuture<List<StudentStatus>> studentStatuses(Test test) {
        return dataLoaderUtil.loadData(test.getStudentStatuses(), DataLoaderUtil.USERTESTSTATUS_LOADER)
                .thenApplyAsync((userTestStatuses) -> {
                    userTestStatuses.sort(getUserTestStatusComparator());
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
