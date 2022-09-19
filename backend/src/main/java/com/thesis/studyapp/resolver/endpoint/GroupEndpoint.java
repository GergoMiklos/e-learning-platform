package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.security.annotation.Authenticated;
import com.thesis.studyapp.service.DefaultGroupService;
import com.thesis.studyapp.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final GroupService groupService;

    public Group group(Long groupId) {
        return groupService.getGroup(groupId);
    }

    @Authenticated
    public Group createGroup(Long userId, NameDescInputDto input) {
        return groupService.createGroup(userId, input);
    }

    @Authenticated
    public Group editGroup(Long groupId, NameDescInputDto input) {
        return groupService.editGroup(groupId, input);
    }

    @Authenticated
    public Group editGroupNews(Long groupId, String news) {
        return groupService.editGroupNews(groupId, news);
    }

    @Authenticated
    public Group addStudentToGroupFromCode(Long userId, String groupCode) {
        return groupService.addStudentToGroupFromCode(userId, groupCode);
    }

    @Authenticated
    public void deleteStudentFromGroup(Long userId, Long groupId) {
        groupService.deleteStudentFromGroup(userId, groupId);
    }

    @Authenticated
    public void deleteTeacherFromGroup(Long userId, Long groupId) {
        groupService.deleteTeacherFromGroup(userId, groupId);
    }

    @Authenticated
    public User addStudentFromCodeToGroup(Long groupId, String studentCode) {
        return groupService.addStudentFromCodeToGroup(groupId, studentCode);
    }

    @Authenticated
    public User addTeacherFromCodeToGroup(Long groupId, String teacherCode) {
        return groupService.addTeacherFromCodeToGroup(groupId, teacherCode);
    }


}
