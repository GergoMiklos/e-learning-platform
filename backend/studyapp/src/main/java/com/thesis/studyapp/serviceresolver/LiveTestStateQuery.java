package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.LiveTestStateRepo;
import com.thesis.studyapp.dto.LiveTestStateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LiveTestStateQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    LiveTestStateRepo liveTestStateRepo;

    public List<LiveTestStateDTO> getByManyIds(List<Long> lTSIds) {
        return liveTestStateRepo.getByManyIds(lTSIds);
    }

    public List<LiveTestStateDTO> getByLiveTestId(Long id) {
        return liveTestStateRepo.getByLiveTestId(id);
    }
}
