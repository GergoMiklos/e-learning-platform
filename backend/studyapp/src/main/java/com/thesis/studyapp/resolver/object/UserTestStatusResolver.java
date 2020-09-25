package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import com.thesis.studyapp.model.UserTestTaskStatus;
import com.thesis.studyapp.util.DataLoaderUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserTestStatusResolver implements GraphQLResolver<UserTestStatus> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<User> user(UserTestStatus userTestStatus) {
        return dataLoaderUtil.loadData(userTestStatus.getUser(), DataLoaderUtil.USER_LOADER);
    }

    public CompletableFuture<Test> test(UserTestStatus userTestStatus) {
        return dataLoaderUtil.loadData(userTestStatus.getTest(), DataLoaderUtil.TEST_LOADER);
    }

    public CompletableFuture<List<UserTestTaskStatus>> userTestTaskStatuses(UserTestStatus userTestStatus) {
        return dataLoaderUtil.loadData(userTestStatus.getUserTestTaskStatuses(), DataLoaderUtil.USERTESTTASKSTATUS_LOADER)
                .thenApplyAsync((userTestTaskStatuses) -> {
                    userTestTaskStatuses.sort(new UserTestTaskStatus.UserTestTaskStatusComparator());
                    return userTestTaskStatuses;
                });
    }

    public CompletableFuture<String> statusChangedTime(UserTestStatus userTestStatus) {
        return CompletableFuture.supplyAsync(() -> DateUtil.convertToIsoString(userTestStatus.getStatusChangedDate()));
    }

    public CompletableFuture<String> status(UserTestStatus userTestStatus) {
        return CompletableFuture.supplyAsync(() -> userTestStatus.getStatus().name());
    }


}
