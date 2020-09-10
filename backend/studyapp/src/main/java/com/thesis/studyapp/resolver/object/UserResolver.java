package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.dto.UserTestStatusDto;
import com.thesis.studyapp.service.GroupService;
import com.thesis.studyapp.service.UserService;
import com.thesis.studyapp.service.UserTestStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class UserResolver implements GraphQLResolver<UserDto> {

    private final GroupService groupService;
    private final UserService userService;
    private final UserTestStatusService userTestStatusService;

    public CompletableFuture<List<GroupDto>> studentGroups(UserDto userDTO) {
        return CompletableFuture.supplyAsync(() ->
                groupService.getGroupsForStudent(userDTO.getId())
        );
    }

    public CompletableFuture<List<GroupDto>> teacherGroups(UserDto userDTO) {
        return CompletableFuture.supplyAsync(() ->
                groupService.getGroupsForTeacher(userDTO.getId())
        );
    }

    public CompletableFuture<List<UserDto>> followedStudents(UserDto userDTO) {
        return CompletableFuture.supplyAsync(() ->
                userService.getStudentsForParent(userDTO.getId())
        );
    }

    public CompletableFuture<List<UserTestStatusDto>> userTestStatuses(UserDto userDTO) {
        return CompletableFuture.supplyAsync(() ->
                userTestStatusService.getUserTestStatusesForUser(userDTO.getId())
        );
    }

}
