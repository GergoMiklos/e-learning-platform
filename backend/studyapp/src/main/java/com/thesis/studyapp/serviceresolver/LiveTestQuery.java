package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.dao.LiveTestRepo;
import com.thesis.studyapp.dao.TestRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.LiveTestDTO;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class LiveTestQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    LiveTestRepo liveTestRepo;

    @Autowired
    TestRepo testRepo;

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    UserRepo userRepo;

    public List<LiveTestDTO> getByManyLiveTestIds(List<Long> ids) {
        return liveTestRepo.getByManyIds(ids);
    }

    @Transactional
    public Optional<LiveTestDTO> createLiveTest(Long groupId, Long testId) {
        Group group = groupRepo.findById(groupId, 0).orElseThrow(()-> new CustomGraphQLException("No group with id: " + groupId));
        Test test = testRepo.findById(testId, 0).orElseThrow(()-> new CustomGraphQLException("No test with id: " + testId));
        LiveTest liveTest = new LiveTest();
        liveTest.setCreationDate(new Date());
        liveTest.setTest(test);
        liveTest.setGroup(group);
        //Todo külön fgv. erre lts.querybe?
        for(User user : userRepo.findByGroupId(groupId)) {
            LiveTestState liveTestState = new LiveTestState();
            liveTestState.setUser(user);
            liveTest.addLiveTestSate(liveTestState);
        }
        liveTest = liveTestRepo.save(liveTest);
        return liveTestRepo.getById(liveTest.getId());
    }
}
