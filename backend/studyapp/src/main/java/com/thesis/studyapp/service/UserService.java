package com.thesis.studyapp.service;

import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(Long userId) {
        return getUserById(userId, 1);
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return userRepository.findByIdIn(ids, 1);
    }

    public List<User> getStudentsForGroup(Long groupId) {
        return userRepository.findByStudentGroupsIdOrderByName(groupId, 1);
    }

    public List<User> getTeachersForGroup(Long groupId) {
        return userRepository.findByTeacherGroupsIdOrderByName(groupId, 1);
    }

    public List<User> getStudentsForParent(Long parentId) {
        return userRepository.findByParentsIdOrderByName(parentId, 1);
    }

    @Transactional
    public User addStudentFromCodeToParent(Long parentId, String studentCode) {
        User student = getUserByCode(studentCode.trim(), 1);
        User parent = getUserById(parentId, 0);
        student.addParent(parent);
        return userRepository.save(student, 1);
    }

    @Transactional
    public void deleteStudentFromParent(Long parentId, Long studentId) {
        if (userRepository.existsById(parentId) && userRepository.existsById(parentId)) {
            userRepository.deleteFollowedStudent(parentId, studentId);
        } else {
            throw new NotFoundException("No user with id: " + parentId + " or " + studentId);
        }
    }

    private User getUserById(Long userId, int depth) {
        return userRepository.findById(userId, depth)
                .orElseThrow(() -> new NotFoundException("No user with id: " + userId));
    }

    private User getUserByCode(String userCode, int depth) {
        return userRepository.findByCodeIgnoreCase(userCode, depth)
                .orElseThrow(() -> new NotFoundException("No user with code: " + userCode));
    }

}
