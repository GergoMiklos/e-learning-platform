package com.thesis.studyapp.util;

import com.thesis.studyapp.model.HasId;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class DataLoaderUtil {
    private static final String PREVENTED_EXCEPTION_MESSAGE = "NullPointerException prevented while using DataLoader with null parameter! " +
            "Maybe forgot to include relationships for object resolvers?";
    private static final String EXCEPTION_OCCURRED_MESSAGE = "Exception occurred while loading data with DataLoader: {}";

    public static final String GROUP_LOADER = "groupLoader";
    public static final String TEST_LOADER = "testLoader";
    public static final String TASK_LOADER = "taskLoader";
    public static final String TESTTASK_LOADER = "testTaskLoader";
    public static final String USER_LOADER = "userLoader";
    public static final String STUDENTSTATUS_LOADER = "studentStatusLoader";
    public static final String STUDENTTASKSTATUS_LOADER = "studentTaskStatusLoader";

    private final DataLoaderRegistry dataLoaderRegistry;
    private final Logger logger = LoggerFactory.getLogger(DataLoaderUtil.class);

    /**
     * Load more objects by the given ids with DataLoader
     * and tries to avoid error exceptions
     */
    public <T extends HasId> CompletableFuture<List<T>> loadData(Set<T> hasIds, String dataLoader) {
        if (hasIds == null) {
            logger.error(PREVENTED_EXCEPTION_MESSAGE);
            return CompletableFuture.completedFuture(new ArrayList<>());
        }

        DataLoader<Long, T> userLoader = dataLoaderRegistry.getDataLoader(dataLoader);
        return userLoader.loadMany(getIds(hasIds))
                .handleAsync((result, exception) -> {
                    if (exception != null) {
                        logger.error(EXCEPTION_OCCURRED_MESSAGE, dataLoader, exception);
                    }
                    if (result == null) {
                        return new ArrayList<>();
                    } else {
                        return result;
                    }
                });
    }

    /**
     * Load on object by the given id with DataLoader
     * and tries to avoid error exceptions
     */
    public <T extends HasId> CompletableFuture<T> loadData(T hasId, String dataLoader) {
        if (hasId == null) {
            logger.error(PREVENTED_EXCEPTION_MESSAGE);
            return CompletableFuture.completedFuture(null);
        }

        DataLoader<Long, T> userLoader = dataLoaderRegistry.getDataLoader(dataLoader);
        return userLoader.load(getId(hasId))
                .handleAsync((result, exception) -> {
                    if (exception != null) {
                        logger.error(EXCEPTION_OCCURRED_MESSAGE, dataLoader, exception);
                    }
                    return result;
                });
    }

    public static <T extends HasId> List<Long> getIds(Set<T> hasIds) {
        return hasIds.stream()
                .map(HasId::getId)
                .collect(Collectors.toList());
    }

    public static <T extends HasId> Long getId(T hasId) {
        return hasId.getId();
    }

}
