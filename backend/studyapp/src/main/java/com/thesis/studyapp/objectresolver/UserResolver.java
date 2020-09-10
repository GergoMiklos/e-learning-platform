package com.thesis.studyapp.objectresolver;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import com.thesis.studyapp.repository.UserTestStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserResolver implements GraphQLResolver<UserDto> {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserTestStatusRepository userTestStatusRepository;

    public CompletableFuture<List<GroupDto>> studentGroups(UserDto userDTO) {
        return CompletableFuture.supplyAsync(() -> groupRepository.getByStudentId(userDTO.getId()));
    }

    public CompletableFuture<List<GroupDto>> teacherGroups(UserDto userDTO) {
        return CompletableFuture.supplyAsync(() -> groupRepository.getByTeacherId(userDTO.getId()));
    }

    public CompletableFuture<List<UserDto>> followedStudents(UserDto userDTO) {
        return CompletableFuture.supplyAsync(() -> userRepository.getFollowedStudents(userDTO.getId()));
    }

    public CompletableFuture<List<UserTestStatusDto>> userTestStatuses(UserDto userDTO) {
        return CompletableFuture.supplyAsync(() -> userTestStatusRepository.getByTestId(userDTO.getId()));
    }

    public CompletableFuture<List<TaskDto>> createdTasks(UserDto userDTO) {
        //Todo loader / külön / ne is legyen? MOST ÉPP NE!
        return null;
    }

}
