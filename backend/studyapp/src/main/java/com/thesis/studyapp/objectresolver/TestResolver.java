package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.*;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class TestResolver implements GraphQLResolver<TestDTO> {
    @Autowired
    DataLoaderRegistry dataLoaderRegistry;

    public CompletableFuture<List<TestTaskDTO>> tasks(TestDTO testDTO) {
        if(testDTO.getTestTaskIds() != null) {
            DataLoader<Long, TestTaskDTO> testtaskloader = dataLoaderRegistry.getDataLoader("testtaskloader");
            return testtaskloader.loadMany(testDTO.getTestTaskIds());
        } else {
            return null;
        }
    }
}
