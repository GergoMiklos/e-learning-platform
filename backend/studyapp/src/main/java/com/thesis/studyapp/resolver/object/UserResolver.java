package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.util.DataLoaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.thesis.studyapp.util.ComparatorUtil.getGroupComparator;
import static com.thesis.studyapp.util.ComparatorUtil.getUserComparator;
import static com.thesis.studyapp.util.ComparatorUtil.getUserTestStatusComparator;

@Component
@RequiredArgsConstructor
public class UserResolver implements GraphQLResolver<User> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<List<Group>> studentGroups(User user) {
        return dataLoaderUtil.loadData(user.getStudentGroups(), DataLoaderUtil.GROUP_LOADER)
                .thenApplyAsync((groups) -> {
                    groups.sort(getGroupComparator());
                    return groups;
                });
    }

    public CompletableFuture<List<Group>> teacherGroups(User user) {
        return dataLoaderUtil.loadData(user.getTeacherGroups(), DataLoaderUtil.GROUP_LOADER)
                .thenApplyAsync((groups) -> {
                    groups.sort(getGroupComparator());
                    return groups;
                });
    }

    public CompletableFuture<List<User>> followedStudents(User user) {
        return dataLoaderUtil.loadData(user.getFollowedStudents(), DataLoaderUtil.USER_LOADER)
                .thenApplyAsync((students) -> {
                    students.sort(getUserComparator());
                    return students;
                });
    }

    public CompletableFuture<List<UserTestStatus>> userTestStatuses(User user) {
        return dataLoaderUtil.loadData(user.getUserTestStatuses(), DataLoaderUtil.USERTESTSTATUS_LOADER)
                .thenApplyAsync((userTestStatuses) -> {
                    userTestStatuses.sort(getUserTestStatusComparator());
                    return userTestStatuses;
                });
    }

}
