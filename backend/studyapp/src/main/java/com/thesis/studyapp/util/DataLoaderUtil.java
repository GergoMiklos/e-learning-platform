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


//Todo külön get minden osztályra?
@Component
@RequiredArgsConstructor
public class DataLoaderUtil {
    public static final String GROUP_LOADER = "groupLoader";
    public static final String TEST_LOADER = "testLoader";
    public static final String TASK_LOADER = "taskLoader";
    public static final String TESTTASK_LOADER = "testTaskLoader";
    public static final String USER_LOADER = "userLoader";
    public static final String USERTESTSTATUS_LOADER = "studentStatusLoader";
    public static final String USERTESTTASKSTATUS_LOADER = "studentTaskStatusLoader";

    private final DataLoaderRegistry dataLoaderRegistry;
    private final Logger logger = LoggerFactory.getLogger(DataLoaderUtil.class);

    public <T extends HasId> CompletableFuture<List<T>> loadData(Set<T> hasIds, String dataLoader) {
        if (hasIds == null) {
            logger.error("NullPointerException prevented while using DataLoader with null parameter! Maybe forgot to include relationships for resolvers?");
            return CompletableFuture.completedFuture(new ArrayList<>());
        }

        DataLoader<Long, T> userLoader = dataLoaderRegistry.getDataLoader(dataLoader);
        return userLoader.loadMany(getIds(hasIds))
                .handleAsync((result, exception) -> {
                    if (exception != null) {
                        logger.error("Exception occurred while loading data with class {} and DataLoader: {}", hasIds.getClass().toString(), dataLoader, exception);
                    }
                    if (result == null) {
                        return new ArrayList<>();
                    } else {
                        return result;
                    }
                });
    }

    public <T extends HasId> CompletableFuture<T> loadData(T hasId, String dataLoader) {
        if (hasId == null) {
            logger.error("NullPointerException (not?) prevented while using DataLoader with null parameter! Maybe forgot to include relationships for resolvers?");
            return CompletableFuture.completedFuture(null);
        }

        DataLoader<Long, T> userLoader = dataLoaderRegistry.getDataLoader(dataLoader);
        return userLoader.load(getId(hasId))
                .handleAsync((result, exception) -> {
                    if (exception != null) {
                        logger.error("Exception occurred while loading data with DataLoader: {}", dataLoader, exception);
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
