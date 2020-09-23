package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final GroupService groupService;

    public GroupDto group(Long groupId) {
        return groupService.getGroup(groupId);
    }

    public GroupDto createGroup(Long userId, NameDescInputDto input) {
        return groupService.createGroup(userId, input);
    }

    public GroupDto editGroup(Long groupId, NameDescInputDto input) {
        return groupService.editGroup(groupId, input);
    }

    public GroupDto changeGroupNews(Long groupId, String news) {
        return groupService.changeGroupNews(groupId, news);
    }

    public GroupDto addStudentToGroupFromCode(Long userId, String groupCode) {
        return groupService.addStudentToGroupFromCode(userId, groupCode);
    }

    public void deleteStudentFromGroup(Long userId, Long groupId) {
        groupService.deleteStudentFromGroup(userId, groupId);
    }

    public void deleteTeacherFromGroup(Long userId, Long groupId) {
        groupService.deleteTeacherFromGroup(userId, groupId);
    }


}
