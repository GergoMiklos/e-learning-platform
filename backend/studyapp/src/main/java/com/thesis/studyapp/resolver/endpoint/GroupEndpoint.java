package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final GroupService groupService;

    public GroupDto getGroup(Long groupId) {
        return groupService.getGroup(groupId);
    }

    public GroupDto createGroup(Long userId, String name, String description) {
        return groupService.createGroup(userId, name, description);
    }

    public GroupDto editGroup(Long groupId, String name, String description) {
        return groupService.editGroup(groupId, name, description);
    }

    public GroupDto changeGroupNews(Long groupId, String news) {
        return groupService.changeGroupNews(groupId, news);
    }

    public GroupDto addStudentToGroupFromCode(Long userId, String groupCode) {
        return groupService.addStudentToGroupFromCode(userId, groupCode);
    }

    public void deleteStudentFromGroup(Long userId, Long groupId) {
        groupService.deleteStudentFromGroup(groupId, userId);
    }

    public void deleteTeacherFromGroup(Long userId, Long groupId) {
        groupService.deleteTeacherFromGroup(groupId, userId);
    }


}
