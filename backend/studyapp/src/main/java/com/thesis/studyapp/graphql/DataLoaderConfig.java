package com.thesis.studyapp.graphql;

import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.service.UserService;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import org.checkerframework.checker.signature.qual.FieldDescriptorForArray;
import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Configuration
public class DataLoaderConfig {
    @Autowired
    UserService userService;

    @Bean
    public DataLoaderRegistry dataLoaderRegistry() {
        System.out.println("Ajjaj másik registry bean!");
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register("userloader", userLoader());
        return dataLoaderRegistry;
    }

    DataLoader<Long, UserDTO> userLoader() {
        BatchLoader<Long, UserDTO> userBatchLoader = new BatchLoader<Long, UserDTO>() {
            @Override
            public CompletionStage<List<UserDTO>> load(List<Long> userIds) {
                return CompletableFuture.supplyAsync(() -> {
                    System.out.println("userBatchLoader: BATCHING!");
                    System.out.println("userIds: "+userIds);
                    List<UserDTO> users = userService.getUserByIds(userIds);
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

//    private  <T> T sortByIds(List<Long> ids, List<T> toSort) {
//        return type.cast(friends.get(name));
//    }

    @Bean
    public Instrumentation instrumentation(DataLoaderRegistry dataLoaderRegistry) {
        return new DataLoaderDispatcherInstrumentation(dataLoaderRegistry);
    }

}
