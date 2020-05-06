package com.thesis.studyapp.graphql.queryresolver;

import com.thesis.studyapp.dao.LiveTestRepo;
import com.thesis.studyapp.dto.LiveTestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LiveTestQuery {
    @Autowired
    LiveTestRepo liveTestRepo;

    public List<LiveTestDTO> getByManyLiveTestIds(List<Long> ids) {
        return liveTestRepo.getByManyIds(ids);
    }
}
