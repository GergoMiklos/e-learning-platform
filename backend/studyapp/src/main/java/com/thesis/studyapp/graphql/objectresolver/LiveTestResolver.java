package com.thesis.studyapp.graphql.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.LiveTestDTO;
import com.thesis.studyapp.dto.LiveTestUserStateDTO;
import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.graphql.queryresolver.LiveTestUserStateQuery;
import com.thesis.studyapp.graphql.queryresolver.TestQuery;
import com.thesis.studyapp.model.LiveTest;
import com.thesis.studyapp.model.LiveTestUserState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class LiveTestResolver implements GraphQLResolver<LiveTestDTO> {
    @Autowired
    LiveTestUserStateQuery liveTestUserStateQuery;
//TODO ne taralmazzuk a tesztet?

    public CompletableFuture<List<LiveTestUserStateDTO>> liveTestUserStates(LiveTestDTO liveTestDTO) {
        return CompletableFuture.supplyAsync(() -> liveTestUserStateQuery.getByLiveTestId(liveTestDTO.getId()));
    }
}
