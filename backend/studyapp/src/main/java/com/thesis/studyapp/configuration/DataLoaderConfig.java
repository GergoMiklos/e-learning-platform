package com.thesis.studyapp.configuration;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.HasId;
import com.thesis.studyapp.model.StudentStatus;
import com.thesis.studyapp.model.StudentTaskStatus;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.TestTask;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.service.GroupService;
import com.thesis.studyapp.service.StudentStatusService;
import com.thesis.studyapp.service.TaskService;
import com.thesis.studyapp.service.TestService;
import com.thesis.studyapp.service.TestTaskService;
import com.thesis.studyapp.service.UserService;
import com.thesis.studyapp.util.DataLoaderUtil;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class DataLoaderConfig {

    private final UserService userService;
    private final TestService testService;
    private final GroupService groupService;
    private final TaskService taskService;
    private final TestTaskService testTaskService;
    private final StudentStatusService studentStatusService;

    @Bean
    public DataLoaderRegistry dataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        return dataLoaderRegistry
                .register(DataLoaderUtil.USER_LOADER, userLoader())
                .register(DataLoaderUtil.GROUP_LOADER, groupLoader())
                .register(DataLoaderUtil.TEST_LOADER, testLoader())
                .register(DataLoaderUtil.TASK_LOADER, taskLoader())
                .register(DataLoaderUtil.TESTTASK_LOADER, testTaskLoader())
                .register(DataLoaderUtil.USERTESTSTATUS_LOADER, userTestStatusLoader())
                .register(DataLoaderUtil.USERTESTTASKSTATUS_LOADER, userTestTaskStatusLoader());
    }

    private org.dataloader.DataLoader<Long, User> userLoader() {
        BatchLoader<Long, User> userBatchLoader = new BatchLoader<Long, User>() {
            @Override
            public CompletionStage<List<User>> load(List<Long> userIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<User> users = userService.getUsersByIds(userIds);
                    return sortListByIds(users, userIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(userBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, Test> testLoader() {
        BatchLoader<Long, Test> testBatchLoader = new BatchLoader<Long, Test>() {
            @Override
            public CompletionStage<List<Test>> load(List<Long> testIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<Test> tests = testService.getTestsByIds(testIds);
                    return sortListByIds(tests, testIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(testBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, Group> groupLoader() {
        BatchLoader<Long, Group> groupBatchLoader = new BatchLoader<Long, Group>() {
            @Override
            public CompletionStage<List<Group>> load(List<Long> groupIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<Group> groups = groupService.getGroupsByIds(groupIds);
                    return sortListByIds(groups, groupIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(groupBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, StudentStatus> userTestStatusLoader() {
        BatchLoader<Long, StudentStatus> batchLoader = new BatchLoader<Long, StudentStatus>() {
            @Override
            public CompletionStage<List<StudentStatus>> load(List<Long> userTestStatusIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<StudentStatus> studentStatuses = studentStatusService.getUserTestStatusesByIds(userTestStatusIds);
                    return sortListByIds(studentStatuses, userTestStatusIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(batchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, StudentTaskStatus> userTestTaskStatusLoader() {
        BatchLoader<Long, StudentTaskStatus> batchLoader = new BatchLoader<Long, StudentTaskStatus>() {
            @Override
            public CompletionStage<List<StudentTaskStatus>> load(List<Long> userTestTaskStatusIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<StudentTaskStatus> userTestStatuses = studentStatusService.getUserTestTaskStatusesByIds(userTestTaskStatusIds);
                    return sortListByIds(userTestStatuses, userTestTaskStatusIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(batchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, Task> taskLoader() {
        BatchLoader<Long, Task> taskBatchLoader = new BatchLoader<Long, Task>() {
            @Override
            public CompletionStage<List<Task>> load(List<Long> taskIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<Task> tasks = taskService.getTasksByIds(taskIds);
                    return sortListByIds(tasks, taskIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(taskBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, TestTask> testTaskLoader() {
        BatchLoader<Long, TestTask> taskBatchLoader = new BatchLoader<Long, TestTask>() {
            @Override
            public CompletionStage<List<TestTask>> load(List<Long> testTaskIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TestTask> testTasks = testTaskService.getTestTasksByIds(testTaskIds);
                    return sortListByIds(testTasks, testTaskIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(taskBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private <T extends HasId> List<T> sortListByIds(List<T> list, List<Long> ids) {
        Map<Long, T> map = list.stream().collect(Collectors.toMap(HasId::getId, t -> t));
        List<T> result = new ArrayList<>();
        for (Long id : ids) {
            result.add(map.get(id));
        }
        return result;
    }

    @Bean
    public Instrumentation instrumentation(DataLoaderRegistry dataLoaderRegistry) {
        return new DataLoaderDispatcherInstrumentation(dataLoaderRegistry);
    }

}
