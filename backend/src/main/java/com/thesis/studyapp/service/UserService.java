package com.thesis.studyapp.service;

import com.thesis.studyapp.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    User getUser(Long userId);

    List<User> getStudentsForGroup(Long groupId);

    List<User> getTeachersForGroup(Long groupId);

    List<User> getStudentsForParent(Long parentId);

    User addStudentFromCodeToParent(Long parentId, String studentCode);

    void deleteStudentFromParent(Long parentId, Long studentId);
}
