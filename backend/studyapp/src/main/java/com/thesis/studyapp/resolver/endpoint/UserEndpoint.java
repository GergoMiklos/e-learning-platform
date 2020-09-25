package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final UserService userService;

    public User user(Long userId) {
        return userService.getUser(userId);
    }

    public User addStudentFromCodeToGroup(Long groupId, String studentCode) {
        return userService.addStudentFromCodeToGroup(groupId, studentCode);
    }

    public User addTeacherFromCodeToGroup(Long groupId, String teacherCode) {
        return userService.addTeacherFromCodeToGroup(groupId, teacherCode);
    }

    public User addStudentFromCodeToParent(Long parentId, String studentCode) {
        return userService.addStudentFromCodeToParent(parentId, studentCode);
    }

    public void deleteStudentFromParent(Long parentId, Long studentId) {
        userService.deleteStudentFromParent(parentId, studentId);
    }


}
