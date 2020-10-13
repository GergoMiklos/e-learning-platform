package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.security.annotation.Authenticated;
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

    @Authenticated
    public User addStudentFromCodeToParent(Long parentId, String studentCode) {
        return userService.addStudentFromCodeToParent(parentId, studentCode);
    }

    @Authenticated
    public void deleteStudentFromParent(Long parentId, Long studentId) {
        userService.deleteStudentFromParent(parentId, studentId);
    }


}
