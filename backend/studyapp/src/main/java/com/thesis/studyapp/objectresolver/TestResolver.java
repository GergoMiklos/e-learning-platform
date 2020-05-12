package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.dto.UserDTO;
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

    public CompletableFuture<List<TaskDTO>> tasks(TestDTO testDTO) {
        DataLoader<Long, TaskDTO> taskloader = dataLoaderRegistry.getDataLoader("taskloader");
        return taskloader.loadMany(testDTO.getTaskIds());
    }
}
