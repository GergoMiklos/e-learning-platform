package com.thesis.studyapp.graphql;

import com.thesis.studyapp.dto.*;
import com.thesis.studyapp.serviceresolver.*;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
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
    //Todo batchService??
    //Todo vagy rakjuk a ..loader függvényeket querybe? NE!
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
    LiveTestStateQuery liveTestStateQuery;

    @Bean
    public DataLoaderRegistry dataLoaderRegistry() {
        System.out.println("Ajjaj másik registry bean!");
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register("userloader", userLoader());
        dataLoaderRegistry.register("newsloader", newsLoader());
        dataLoaderRegistry.register("grouploader", groupLoader());
        dataLoaderRegistry.register("testloader", testLoader());
        dataLoaderRegistry.register("taskloader", taskLoader());
        dataLoaderRegistry.register("liveteststateloader", liveTestStateLoader());
        dataLoaderRegistry.register("livetestloader", liveTestLoader());
        return dataLoaderRegistry;
    }

    private DataLoader<Long, UserDTO> userLoader() {
        BatchLoader<Long, UserDTO> userBatchLoader = new BatchLoader<Long, UserDTO>() {
            @Override
            public CompletionStage<List<UserDTO>> load(List<Long> userIds) {
                return CompletableFuture.supplyAsync(() -> {
                    System.out.println("userBatchLoader: BATCHING!");
                    System.out.println("userIds: "+userIds);
                    List<UserDTO> users = userQuery.getManyByIds(userIds);
                    List<UserDTO> result = new ArrayList<>();
                    for(Long id: userIds) {
                        for (UserDTO u : users) {
                            if(u.getId().equals(id)) {
                                result.add(u);
                            }
                        }
                    }
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(userBatchLoader);
    }

    private DataLoader<Long, NewsDTO> newsLoader() {
        BatchLoader<Long, NewsDTO> newsBatchLoader = new BatchLoader<Long, NewsDTO>() {
            @Override
            public CompletionStage<List<NewsDTO>> load(List<Long> newsIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<NewsDTO> news = newsQuery.getByManyNewsIds(newsIds);
                    List<NewsDTO> result = new ArrayList<>();
                    for(Long id: newsIds) {
                        for (NewsDTO n : news) {
                            if(n.getId().equals(id)) {
                                result.add(n);
                                break;
                            }
                        }
                    }
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(newsBatchLoader);
    }

    private DataLoader<Long, TestDTO> testLoader() {
        BatchLoader<Long, TestDTO> newsBatchLoader = new BatchLoader<Long, TestDTO>() {
            @Override
            public CompletionStage<List<TestDTO>> load(List<Long> testIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TestDTO> tests = testQuery.getByManyTestIds(testIds);
                    List<TestDTO> result = new ArrayList<>();
                    for(Long id: testIds) {
                        for (TestDTO t : tests) {
                            if(t.getId().equals(id)) {
                                result.add(t);
                                break;
                            }
                        }
                    }
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(newsBatchLoader);
    }

    private DataLoader<Long, LiveTestDTO> liveTestLoader() {
        BatchLoader<Long, LiveTestDTO> newsBatchLoader = new BatchLoader<Long, LiveTestDTO>() {
            @Override
            public CompletionStage<List<LiveTestDTO>> load(List<Long> testIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<LiveTestDTO> tests = liveTestQuery.getByManyLiveTestIds(testIds);
                    List<LiveTestDTO> result = new ArrayList<>();
                    for(Long id: testIds) {
                        for (LiveTestDTO t : tests) {
                            if(t.getId().equals(id)) {
                                result.add(t);
                                break;
                            }
                        }
                    }
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(newsBatchLoader);
    }

    private DataLoader<Long, GroupDTO> groupLoader() {
        BatchLoader<Long, GroupDTO> newsBatchLoader = new BatchLoader<Long, GroupDTO>() {
            @Override
            public CompletionStage<List<GroupDTO>> load(List<Long> groupIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<GroupDTO> tests = groupQuery.getByManyGroupIds(groupIds);
                    List<GroupDTO> result = new ArrayList<>();
                    for(Long id: groupIds) {
                        for (GroupDTO t : tests) {
                            if(t.getId().equals(id)) {
                                result.add(t);
                                break;
                            }
                        }
                    }
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(newsBatchLoader);
    }

    private DataLoader<Long, LiveTestStateDTO> liveTestStateLoader() {
        BatchLoader<Long, LiveTestStateDTO> newsBatchLoader = new BatchLoader<Long, LiveTestStateDTO>() {
            @Override
            public CompletionStage<List<LiveTestStateDTO>> load(List<Long> groupIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<LiveTestStateDTO> tests = liveTestStateQuery.getByManyIds(groupIds);
                    List<LiveTestStateDTO> result = new ArrayList<>();
                    for(Long id: groupIds) {
                        for (LiveTestStateDTO t : tests) {
                            if(t.getId().equals(id)) {
                                result.add(t);
                                break;
                            }
                        }
                    }
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(newsBatchLoader);
    }

    private DataLoader<Long, TaskDTO> taskLoader() {
        BatchLoader<Long, TaskDTO> newsBatchLoader = new BatchLoader<Long, TaskDTO>() {
            @Override
            public CompletionStage<List<TaskDTO>> load(List<Long> groupIds) {
                return CompletableFuture.supplyAsync(() -> {
                    List<TaskDTO> tests = taskQuery.getByManyIds(groupIds);
                    List<TaskDTO> result = new ArrayList<>();
                    for(Long id: groupIds) {
                        for (TaskDTO t : tests) {
                            if(t.getId().equals(id)) {
                                result.add(t);
                                break;
                            }
                        }
                    }
                    return result;
                });
            }
        };
        return DataLoader.newDataLoader(newsBatchLoader);
    }

//    private  <T> T sortByIds(List<Long> ids, List<T> toSort) {
//        return type.cast(friends.get(name));
//    }

    @Bean
    public Instrumentation instrumentation(DataLoaderRegistry dataLoaderRegistry) {
        return new DataLoaderDispatcherInstrumentation(dataLoaderRegistry);
    }

}
