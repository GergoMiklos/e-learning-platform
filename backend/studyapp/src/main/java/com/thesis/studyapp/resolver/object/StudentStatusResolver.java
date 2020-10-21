package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.model.StudentTaskStatus;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.util.DataLoaderUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.thesis.studyapp.util.ComparatorUtil.getStudentTaskStatusComparator;

@Component
@RequiredArgsConstructor
public class StudentStatusResolver implements GraphQLResolver<StudentStatus> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<TestTask> currentTestTask(StudentStatus studentStatus) {
        if (studentStatus.getCurrentTestTask() != null) {
            return dataLoaderUtil.loadData(studentStatus.getCurrentTestTask(), DataLoaderUtil.TESTTASK_LOADER);
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    public CompletableFuture<User> user(StudentStatus studentStatus) {
        return dataLoaderUtil.loadData(studentStatus.getUser(), DataLoaderUtil.USER_LOADER);
    }

    public CompletableFuture<Test> test(StudentStatus studentStatus) {
        return dataLoaderUtil.loadData(studentStatus.getTest(), DataLoaderUtil.TEST_LOADER);
    }

    public CompletableFuture<List<StudentTaskStatus>> studentTaskStatuses(StudentStatus studentStatus) {
        return dataLoaderUtil.loadData(studentStatus.getStudentTaskStatuses(), DataLoaderUtil.STUDENTTASKSTATUS_LOADER)
                .thenApplyAsync((studentTaskStatuses) -> {
                    studentTaskStatuses.sort(getStudentTaskStatusComparator());
                    return studentTaskStatuses;
                });
    }

    public CompletableFuture<String> statusChangedTime(StudentStatus studentStatus) {
        return CompletableFuture.completedFuture(DateUtil.convertToIsoString(studentStatus.getStatusChangedDate()));
    }

    public CompletableFuture<String> lastSolutionTime(StudentStatus studentStatus) {
        return CompletableFuture.completedFuture(DateUtil.convertToIsoString(studentStatus.getLastSolutionTime()));
    }

    public CompletableFuture<String> status(StudentStatus studentStatus) {
        return CompletableFuture.completedFuture(studentStatus.getStatus().name());
    }

    public CompletableFuture<Integer> solvedTasks(StudentStatus studentStatus) {
        return CompletableFuture.completedFuture(
                studentStatus.getStudentTaskStatuses() == null ? 0 : studentStatus.getStudentTaskStatuses().size());
    }


}
