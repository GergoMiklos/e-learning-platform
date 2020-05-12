package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.LiveTestDTO;
import com.thesis.studyapp.dto.LiveTestStateDTO;
import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.serviceresolver.LiveTestStateQuery;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class LiveTestResolver implements GraphQLResolver<LiveTestDTO> {
    @Autowired
    LiveTestStateQuery liveTestStateQuery;

    @Autowired
    DataLoaderRegistry dataLoaderRegistry;

    //Todo jó ez így?
    public CompletableFuture<List<LiveTestStateDTO>> liveTestStates(LiveTestDTO liveTestDTO) {
        return CompletableFuture.supplyAsync(() -> liveTestStateQuery.getByLiveTestId(liveTestDTO.getId()));
    }

    public CompletableFuture<TestDTO> test(LiveTestDTO liveTestDTO) {
        if(liveTestDTO.getTestId() != null) {
            DataLoader<Long, TestDTO> testloader = dataLoaderRegistry.getDataLoader("testloader");
            return testloader.load(liveTestDTO.getTestId());
        } else {
            return null;
        }
    }
}
