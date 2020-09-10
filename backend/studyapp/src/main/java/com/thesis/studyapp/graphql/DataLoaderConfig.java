package com.thesis.studyapp.graphql;

import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.HasId;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.serviceresolver.GroupEndpoint;
import com.thesis.studyapp.serviceresolver.TaskEndpoint;
import com.thesis.studyapp.serviceresolver.TestEndpoint;
import com.thesis.studyapp.serviceresolver.TestTaskEndpoint;
import com.thesis.studyapp.serviceresolver.UserEndpoint;
import com.thesis.studyapp.serviceresolver.UserTestStatusEndpoint;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import lombok.RequiredArgsConstructor;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Configuration
@RequiredArgsConstructor
public class DataLoaderConfig {

    private final UserEndpoint userEndpoint;
    private final TestEndpoint testEndpoint;
    private final GroupEndpoint groupEndpoint;
    private final TaskEndpoint taskEndpoint;
    private final TestTaskEndpoint testTaskEndpoint;
    private final UserTestStatusEndpoint userTestStatusEndpoint;

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

    private DataLoader<Long, UserDto> userLoader() {
        BatchLoader<Long, UserDto> userBatchLoader = new BatchLoader<Long, UserDto>() {
            @Override
            public CompletionStage<List<UserDto>> load(List<Long> userIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<UserDto> users = userEndpoint.getUsersByIds(userIds);
                    return sortByIds(userIds, users);
                });
            }
        };
        return DataLoader.newDataLoader(userBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, TestDto> testLoader() {
        BatchLoader<Long, TestDto> testBatchLoader = new BatchLoader<Long, TestDto>() {
            @Override
            public CompletionStage<List<TestDto>> load(List<Long> testIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TestDto> tests = testEndpoint.getTestsByIds(testIds);
                    return sortByIds(testIds, tests);
                });
            }
        };
        return DataLoader.newDataLoader(testBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, GroupDto> groupLoader() {
        BatchLoader<Long, GroupDto> groupBatchLoader = new BatchLoader<Long, GroupDto>() {
            @Override
            public CompletionStage<List<GroupDto>> load(List<Long> groupIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<GroupDto> groups = groupEndpoint.getGroupsByIds(groupIds);
                    return sortByIds(groupIds, groups);
                });
            }
        };
        return DataLoader.newDataLoader(groupBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, UserTestStatusDto> userTestStateLoader() {
        BatchLoader<Long, UserTestStatusDto> batchLoader = new BatchLoader<Long, UserTestStatusDto>() {
            @Override
            public CompletionStage<List<UserTestStatusDto>> load(List<Long> userTestStatusIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<UserTestStatusDto> userTestStatuses = userTestStatusEndpoint.getUserTestStatusesByIds(userTestStatusIds);
                    return sortByIds(userTestStatusIds, userTestStatuses);
                });
            }
        };
        return DataLoader.newDataLoader(batchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, TaskDto> taskLoader() {
        BatchLoader<Long, TaskDto> taskBatchLoader = new BatchLoader<Long, TaskDto>() {
            @Override
            public CompletionStage<List<TaskDto>> load(List<Long> taskIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TaskDto> tasks = taskEndpoint.getTasksByIds(taskIds);
                    return sortByIds(taskIds, tasks);
                });
            }
        };
        return DataLoader.newDataLoader(taskBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, TestTaskDto> testTaskLoader() {
        BatchLoader<Long, TestTaskDto> taskBatchLoader = new BatchLoader<Long, TestTaskDto>() {
            @Override
            public CompletionStage<List<TestTaskDto>> load(List<Long> testTaskIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TestTaskDto> testTasks = testTaskEndpoint.getTestTasksByIds(testTaskIds);
                    return sortByIds(testTaskIds, testTasks);
                });
            }
        };
        return DataLoader.newDataLoader(taskBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private  <T extends HasId> List<T> sortByIds(List<Long> ids, List<T> toSort) {
        List<T> result = new ArrayList<>();
        for(Long id: ids) {
            for (T hasId : toSort) {
                if (hasId.getId().equals(id)) {
                    result.add(hasId);
                    break;
                }
            }
        }
        return result;
    }

    @Bean
    public Instrumentation instrumentation(DataLoaderRegistry dataLoaderRegistry) {
        return new DataLoaderDispatcherInstrumentation(dataLoaderRegistry);
    }

}
