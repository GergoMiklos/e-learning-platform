package com.thesis.studyapp.configuration.graphql;

import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.HasId;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.service.GroupService;
import com.thesis.studyapp.service.TaskService;
import com.thesis.studyapp.service.TestService;
import com.thesis.studyapp.service.TestTaskService;
import com.thesis.studyapp.service.UserService;
import com.thesis.studyapp.service.UserTestStatusService;
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
public class DataLoaderConfiguration {

    private final UserService userService;
    private final TestService testService;
    private final GroupService groupService;
    private final TaskService taskService;
    private final TestTaskService testTaskService;
    private final UserTestStatusService userTestStatusService;

    @Bean
    public DataLoaderRegistry dataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        return dataLoaderRegistry
                .register("userLoader", userLoader())
                .register("groupLoader", groupLoader())
                .register("testLoader", testLoader())
                .register("taskLoader", taskLoader())
                .register("testTaskLoader", testTaskLoader())
                .register("userTestSateLoader", userTestStateLoader());
    }

    private org.dataloader.DataLoader<Long, UserDto> userLoader() {
        BatchLoader<Long, UserDto> userBatchLoader = new BatchLoader<Long, UserDto>() {
            @Override
            public CompletionStage<List<UserDto>> load(List<Long> userIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<UserDto> users = userService.getUsersByIds(userIds);
                    return sortListByIds(users, userIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(userBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, TestDto> testLoader() {
        BatchLoader<Long, TestDto> testBatchLoader = new BatchLoader<Long, TestDto>() {
            @Override
            public CompletionStage<List<TestDto>> load(List<Long> testIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TestDto> tests = testService.getTestsByIds(testIds);
                    return sortListByIds(tests, testIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(testBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, GroupDto> groupLoader() {
        BatchLoader<Long, GroupDto> groupBatchLoader = new BatchLoader<Long, GroupDto>() {
            @Override
            public CompletionStage<List<GroupDto>> load(List<Long> groupIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<GroupDto> groups = groupService.getGroupsByIds(groupIds);
                    return sortListByIds(groups, groupIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(groupBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, UserTestStatusDto> userTestStateLoader() {
        BatchLoader<Long, UserTestStatusDto> batchLoader = new BatchLoader<Long, UserTestStatusDto>() {
            @Override
            public CompletionStage<List<UserTestStatusDto>> load(List<Long> userTestStatusIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<UserTestStatusDto> userTestStatuses = userTestStatusService.getUserTestStatusesByIds(userTestStatusIds);
                    return sortListByIds(userTestStatuses, userTestStatusIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(batchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, TaskDto> taskLoader() {
        BatchLoader<Long, TaskDto> taskBatchLoader = new BatchLoader<Long, TaskDto>() {
            @Override
            public CompletionStage<List<TaskDto>> load(List<Long> taskIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TaskDto> tasks = taskService.getTasksByIds(taskIds);
                    return sortListByIds(tasks, taskIds);
                });
            }
        };
        return org.dataloader.DataLoader.newDataLoader(taskBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private org.dataloader.DataLoader<Long, TestTaskDto> testTaskLoader() {
        BatchLoader<Long, TestTaskDto> taskBatchLoader = new BatchLoader<Long, TestTaskDto>() {
            @Override
            public CompletionStage<List<TestTaskDto>> load(List<Long> testTaskIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TestTaskDto> testTasks = testTaskService.getTestTasksByIds(testTaskIds);
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
