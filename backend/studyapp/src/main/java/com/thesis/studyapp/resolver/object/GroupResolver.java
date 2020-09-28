package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.util.DataLoaderUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.thesis.studyapp.util.ComparatorUtil.getTestComparator;
import static com.thesis.studyapp.util.ComparatorUtil.getUserComparator;

@Component
@RequiredArgsConstructor
public class GroupResolver implements GraphQLResolver<Group> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<List<User>> students(Group group) {
        return dataLoaderUtil.loadData(group.getStudents(), DataLoaderUtil.USER_LOADER)
                .thenApplyAsync((students) -> {
                    students.sort(getUserComparator());
                    return students;
                });
    }

    public CompletableFuture<List<User>> teachers(Group group) {
        return dataLoaderUtil.loadData(group.getTeachers(), DataLoaderUtil.USER_LOADER)
                .thenApplyAsync((teachers) -> {
                    teachers.sort(getUserComparator());
                    return teachers;
                });
    }

    public CompletableFuture<List<Test>> tests(Group group) {
        return dataLoaderUtil.loadData(group.getTests(), DataLoaderUtil.TEST_LOADER)
                .thenApplyAsync((tests) -> {
                    tests.sort(getTestComparator());
                    return tests;
                });
    }

    public CompletableFuture<String> newsChangedDate(Group group) {
        return CompletableFuture.completedFuture(DateUtil.convertToIsoString(group.getNewsChangedDate()));
    }

}
