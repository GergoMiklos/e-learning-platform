package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.StudentTaskStatus;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.util.DataLoaderUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class StudentTaskStatusResolver implements GraphQLResolver<StudentTaskStatus> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<TestTask> testTask(StudentTaskStatus studentTaskStatus) {
        return dataLoaderUtil.loadData(studentTaskStatus.getTestTask(), DataLoaderUtil.TESTTASK_LOADER);
    }

    public CompletableFuture<String> lastSolutionTime(StudentTaskStatus studentTaskStatus) {
        return CompletableFuture.completedFuture(DateUtil.convertToIsoString(studentTaskStatus.getLastSolutionTime()));
    }
}
