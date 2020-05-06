package com.thesis.studyapp.graphql.queryresolver;

import com.thesis.studyapp.dao.LiveTestUserStateRepo;
import com.thesis.studyapp.dto.LiveTestUserStateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class LiveTestUserStateQuery {
    @Autowired
    LiveTestUserStateRepo liveTestUserStateRepo;

    public List<LiveTestUserStateDTO> getByManyIds(List<Long> lTSIds) {
        return liveTestUserStateRepo.findByManyIds(lTSIds);
    }

    public List<LiveTestUserStateDTO> getByLiveTestId(Long id) {
        return liveTestUserStateRepo.findByLiveTestId(id);
    }
}
