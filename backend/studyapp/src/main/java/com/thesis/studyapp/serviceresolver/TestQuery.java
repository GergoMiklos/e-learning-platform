package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.TestRepo;
import com.thesis.studyapp.dto.TestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TestQuery implements GraphQLQueryResolver {
    @Autowired
    TestRepo testRepo;

    public List<TestDTO> getByManyTestIds(List<Long> testIds) {
        return testRepo.getByManyIds(testIds);
    }

    public Optional<TestDTO> test(Long testId) {
        return testRepo.getById(testId);
    }
}
