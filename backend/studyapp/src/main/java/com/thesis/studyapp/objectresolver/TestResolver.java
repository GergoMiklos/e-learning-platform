package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.repository.TestTaskRepository;
import com.thesis.studyapp.repository.UserTestStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TestResolver implements GraphQLResolver<TestDto> {

    private final UserTestStatusRepository userTestStatusRepository;
    private final TestTaskRepository testTaskRepository;

    public CompletableFuture<List<TestTaskDto>> tasks(TestDto testDTO) {
        return CompletableFuture.supplyAsync(() -> testTaskRepository.getByTestId(testDTO.getId()));
    }

    public CompletableFuture<List<UserTestStatusDto>> userTestStatuses(TestDto testDTO) {
        return CompletableFuture.supplyAsync(() -> userTestStatusRepository.getByTestId(testDTO.getId()));
    }
}
