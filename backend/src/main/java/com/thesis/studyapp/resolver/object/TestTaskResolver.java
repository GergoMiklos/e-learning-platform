package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.util.DataLoaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TestTaskResolver implements GraphQLResolver<TestTask> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<Task> task(TestTask testTask) {
        return dataLoaderUtil.loadData(testTask.getTask(), DataLoaderUtil.TASK_LOADER);
    }


}
