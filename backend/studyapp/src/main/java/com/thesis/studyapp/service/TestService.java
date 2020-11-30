package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.model.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TestService {
    Test getTest(Long testId);

    List<Test> getTestsForGroup(Long groupId);

    Test createTest(Long groupId, NameDescInputDto input);

    Test editTestStatus(Long testId, boolean active);

    Test editTest(Long testId, NameDescInputDto input);
}
