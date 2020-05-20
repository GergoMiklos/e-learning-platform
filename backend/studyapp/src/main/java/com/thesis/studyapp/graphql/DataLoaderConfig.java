package com.thesis.studyapp.graphql;

import com.thesis.studyapp.HasId;
import com.thesis.studyapp.dto.*;
import com.thesis.studyapp.serviceresolver.*;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderOptions;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Configuration
public class DataLoaderConfig {
    //Todo batchService/Repo?
    //Todo vagy rakjuk a ..loader függvényeket querybe?
    @Autowired
    UserQuery userQuery;
    @Autowired
    NewsQuery newsQuery;
    @Autowired
    TestQuery testQuery;
    @Autowired
    LiveTestQuery liveTestQuery;
    @Autowired
    GroupQuery groupQuery;
    @Autowired
    TaskQuery taskQuery;
    @Autowired
    TestTaskQuery testTaskQuery;
    @Autowired
    LiveTestStateQuery liveTestStateQuery;

    @Bean
    public DataLoaderRegistry dataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        return dataLoaderRegistry
                .register("userloader", userLoader())
                .register("newsloader", newsLoader())
                .register("grouploader", groupLoader())
                .register("testloader", testLoader())
                .register("taskloader", taskLoader())
                .register("testtaskloader", testTaskLoader())
                .register("liveteststateloader", liveTestStateLoader())
                .register("livetestloader", liveTestLoader());
    }

    private DataLoader<Long, UserDTO> userLoader() {
        BatchLoader<Long, UserDTO> userBatchLoader = new BatchLoader<Long, UserDTO>() {
            @Override
            public CompletionStage<List<UserDTO>> load(List<Long> userIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<UserDTO> users = userQuery.getManyByIds(userIds);
                    return sortByIds(userIds, users);
                });
            }
        };
        return DataLoader.newDataLoader(userBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, NewsDTO> newsLoader() {
        BatchLoader<Long, NewsDTO> newsBatchLoader = new BatchLoader<Long, NewsDTO>() {
            @Override
            public CompletionStage<List<NewsDTO>> load(List<Long> newsIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<NewsDTO> news = newsQuery.getByManyNewsIds(newsIds);
                    return sortByIds(newsIds, news);
                });
            }
        };
        return DataLoader.newDataLoader(newsBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, TestDTO> testLoader() {
        BatchLoader<Long, TestDTO> testBatchLoader = new BatchLoader<Long, TestDTO>() {
            @Override
            public CompletionStage<List<TestDTO>> load(List<Long> testIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TestDTO> tests = testQuery.getByManyTestIds(testIds);
                    return  sortByIds(testIds, tests);
                });
            }
        };
        return DataLoader.newDataLoader(testBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, LiveTestDTO> liveTestLoader() {
        BatchLoader<Long, LiveTestDTO> liveTestBatchLoader = new BatchLoader<Long, LiveTestDTO>() {
            @Override
            public CompletionStage<List<LiveTestDTO>> load(List<Long> liveTestIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<LiveTestDTO> liveTests = liveTestQuery.getByManyLiveTestIds(liveTestIds);
                    return sortByIds(liveTestIds, liveTests);
                });
            }
        };
        return DataLoader.newDataLoader(liveTestBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, GroupDTO> groupLoader() {
        BatchLoader<Long, GroupDTO> groupBatchLoader = new BatchLoader<Long, GroupDTO>() {
            @Override
            public CompletionStage<List<GroupDTO>> load(List<Long> groupIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<GroupDTO> groups = groupQuery.getByManyGroupIds(groupIds);
                    return sortByIds(groupIds, groups);
                });
            }
        };
        return DataLoader.newDataLoader(groupBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, LiveTestStateDTO> liveTestStateLoader() {
        BatchLoader<Long, LiveTestStateDTO> liveTestStateBatchLoader = new BatchLoader<Long, LiveTestStateDTO>() {
            @Override
            public CompletionStage<List<LiveTestStateDTO>> load(List<Long> liveTestStateIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<LiveTestStateDTO> liveTestStates = liveTestStateQuery.getByManyIds(liveTestStateIds);
                    return sortByIds(liveTestStateIds, liveTestStates);
                });
            }
        };
        return DataLoader.newDataLoader(liveTestStateBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, TaskDTO> taskLoader() {
        BatchLoader<Long, TaskDTO> taskBatchLoader = new BatchLoader<Long, TaskDTO>() {
            @Override
            public CompletionStage<List<TaskDTO>> load(List<Long> taskIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TaskDTO> tasks = taskQuery.getByManyIds(taskIds);
                    return sortByIds(taskIds, tasks);
                });
            }
        };
        return DataLoader.newDataLoader(taskBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private DataLoader<Long, TestTaskDTO> testTaskLoader() {
        BatchLoader<Long, TestTaskDTO> taskBatchLoader = new BatchLoader<Long, TestTaskDTO>() {
            @Override
            public CompletionStage<List<TestTaskDTO>> load(List<Long> testTaskIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TestTaskDTO> testTasks = testTaskQuery.getByManyIds(testTaskIds);
                    return sortByIds(testTaskIds, testTasks);
                });
            }
        };
        return DataLoader.newDataLoader(taskBatchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private  <T extends HasId> List<T> sortByIds(List<Long> ids, List<T> toSort) {
        List<T> result = new ArrayList<>();
        for(Long id: ids) {
            for (T t : toSort) {
                if(t.getId().equals(id)) {
                    result.add(t);
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
