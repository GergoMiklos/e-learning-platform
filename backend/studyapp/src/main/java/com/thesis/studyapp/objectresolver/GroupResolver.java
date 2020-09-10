package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.repository.TestRepository;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class GroupResolver implements GraphQLResolver<GroupDto> {

    private final UserRepository userRepository;
    private final TestRepository testRepository;

    public CompletableFuture<List<UserDto>> students(GroupDto groupDTO) {
        return CompletableFuture.supplyAsync(() -> userRepository.getStudentByGroupId(groupDTO.getId()));
        //TODO melyik?
        //return CompletableFuture.completedFuture(userRepository.getStudentByGroupId(groupDTO.getId()));
    }

    public CompletableFuture<List<UserDto>> teachers(GroupDto groupDTO) {
        return CompletableFuture.supplyAsync(() -> userRepository.getTeacherByGroupId(groupDTO.getId()));
    }

    public CompletableFuture<List<TestDto>> tests(GroupDto groupDTO) {
        return CompletableFuture.supplyAsync(() -> testRepository.getByGroupId(groupDTO.getId()));
    }

    public String newsChangedDate(GroupDto groupDTO) {
        return groupDTO.getNewsChangedDate().toString();
    }

}
