package com.thesis.studyapp.configuration;

import com.thesis.studyapp.model.HasId;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.ObjectLoader;
import com.thesis.studyapp.repository.StudentStatusRepository;
import com.thesis.studyapp.repository.StudentTaskStatusRepository;
import com.thesis.studyapp.repository.TaskRepository;
import com.thesis.studyapp.repository.TestRepository;
import com.thesis.studyapp.repository.TestTaskRepository;
import com.thesis.studyapp.repository.UserRepository;
import com.thesis.studyapp.util.DataLoaderUtil;
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
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class DataLoaderConfig {

    private final UserRepository userRepository;
    private final TestRepository testRepository;
    private final GroupRepository groupRepository;
    private final TaskRepository taskRepository;
    private final TestTaskRepository testTaskRepository;
    private final StudentStatusRepository studentStatusRepository;
    private final StudentTaskStatusRepository studentTaskStatusRepository;

    @Bean
    public DataLoaderRegistry dataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        return dataLoaderRegistry
                .register(DataLoaderUtil.USER_LOADER, createLoader(userRepository))
                .register(DataLoaderUtil.GROUP_LOADER, createLoader(groupRepository))
                .register(DataLoaderUtil.TEST_LOADER, createLoader(testRepository))
                .register(DataLoaderUtil.TASK_LOADER, createLoader(taskRepository))
                .register(DataLoaderUtil.TESTTASK_LOADER, createLoader(testTaskRepository))
                .register(DataLoaderUtil.STUDENTSTATUS_LOADER, createLoader(studentStatusRepository))
                .register(DataLoaderUtil.STUDENTTASKSTATUS_LOADER, createLoader(studentTaskStatusRepository));
    }

    @Bean
    public DataLoaderDispatcherInstrumentation dataLoaderInstrumentation(DataLoaderRegistry dataLoaderRegistry) {
        return new DataLoaderDispatcherInstrumentation(dataLoaderRegistry);
    }

    /**
     * Create a new Loader which
     * loads the objects for the given ids and
     * then (necessarily) sorts them by the given ids
     */
    private <T extends HasId, L extends ObjectLoader<T>> DataLoader<Long, T> createLoader(L objectLoader) {
        BatchLoader<Long, T> batchLoader = new BatchLoader<Long, T>() {
            @Override
            public CompletionStage<List<T>> load(List<Long> ids) {
                return CompletableFuture.supplyAsync(() -> {
                    List<T> objects = objectLoader.findByIdIn(ids, 1);
                    return sortByIds(objects, ids);
                });
            }
        };
        return DataLoader.newDataLoader(batchLoader, DataLoaderOptions.newOptions().setCachingEnabled(false));
    }

    private <T extends HasId> List<T> sortByIds(List<T> list, List<Long> ids) {
        Map<Long, T> map = list.stream().collect(Collectors.toMap(HasId::getId, object -> object));
        List<T> result = new ArrayList<>();
        for (Long id : ids) {
            result.add(map.get(id));
        }
        return result;
    }

}
