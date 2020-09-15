package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.configuration.DateUtil;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.service.TestService;
import com.thesis.studyapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class GroupResolver implements GraphQLResolver<GroupDto> {

    private final UserService userService;
    private final TestService testService;

    private final DateUtil dateUtil;

    public CompletableFuture<List<UserDto>> students(GroupDto groupDTO) {
        return CompletableFuture.supplyAsync(() ->
                userService.getStudentsForGroup(groupDTO.getId())
        );
    }

    public CompletableFuture<List<UserDto>> teachers(GroupDto groupDTO) {
        return CompletableFuture.supplyAsync(() ->
                userService.getTeachersForGroup(groupDTO.getId())
        );
    }

    public CompletableFuture<List<TestDto>> tests(GroupDto groupDTO) {
        return CompletableFuture.supplyAsync(() ->
                testService.getTestsForGroup(groupDTO.getId())
        );
    }

    public CompletableFuture<String> newsChangedDate(GroupDto groupDTO) {
        //Todo null pointer exception
        LocalDateTime newsChangedDate = groupDTO.getNewsChangedDate();
        return CompletableFuture.completedFuture(newsChangedDate == null ? null : dateUtil.convertToIsoString(newsChangedDate));
    }

}
