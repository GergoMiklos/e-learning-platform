package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final UserService userService;

    public UserDto getUser(Long userId) {
        return userService.getUser(userId);
    }

    public UserDto addStudentFromCodeToGroup(Long groupId, String studentCode) {
        return userService.addStudentFromCodeToGroup(groupId, studentCode);
    }

    public UserDto addTeacherFromCodeToGroup(Long groupId, String teacherCode) {
        return userService.addTeacherFromCodeToGroup(groupId, teacherCode);
    }

    public UserDto addStudentFromCodeToParent(Long parentId, String studentCode) {
        return userService.addStudentFromCodeToParent(parentId, studentCode);
    }

    public void deleteStudentFromParent(Long parentId, Long studentId) {
        userService.deleteStudentFromParent(parentId, studentId);
    }


}
