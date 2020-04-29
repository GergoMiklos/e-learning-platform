package com.thesis.studyapp.graphql.queryresolver;

import com.thesis.studyapp.dao.TestRepo;
import com.thesis.studyapp.dto.TestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestQuery {
    @Autowired
    TestRepo testRepo;

    public List<TestDTO> getByManyTestIds(List<Long> testIds) {
        return testRepo.getByManyIds(testIds);
    }
}
