package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public UserDto getUser(Long userId) {
        return UserDto.build(getUserById(userId, 0));
    }

    public List<UserDto> getUsersByIds(List<Long> ids) {
        return userRepository.findByIdIn(ids, 0).stream()
                .map(UserDto::build)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto addStudentFromCodeToGroup(Long groupId, String studentCode) {
        User user = getUserByCode(studentCode, 1);
        Group group = getGroupById(groupId, 0);
        user.getStudentGroups().add(group);
        return UserDto.build(userRepository.save(user));
    }

    @Transactional
    public UserDto addTeacherFromCodeToGroup(Long groupId, String teacherCode) {
        User user = getUserByCode(teacherCode, 1);
        Group group = getGroupById(groupId, 0);
        user.getTeacherGroups().add(group);
        return UserDto.build(userRepository.save(user));
    }

    @Transactional
    public UserDto addStudentFromCodeToParent(Long parentId, String studentCode) {
        User student = getUserByCode(studentCode, 0);
        User parent = getUserById(parentId, 1);
        student.getParents().add(parent);
        return UserDto.build(userRepository.save(student));
    }

    @Transactional
    public void deleteStudentFromParent(Long parentId, Long studentId) {
        if (userRepository.existsById(parentId) && userRepository.existsById(parentId)) {
            userRepository.deleteFollowedStudent(parentId, studentId);
        } else {
            throw new CustomGraphQLException("No user with id: " + parentId + " or " + studentId);
        }
    }

    public List<UserDto> getStudentsForGroup(Long groupId) {
        return convertToDto(userRepository.findByStudentGroupsIdOrderByName(groupId, 0));
    }

    public List<UserDto> getTeachersForGroup(Long groupId) {
        return convertToDto(userRepository.findByTeacherGroupsIdOrderByName(groupId, 0));
    }

    public List<UserDto> getStudentsForParent(Long parentId) {
        return convertToDto(userRepository.findByParentsIdOrderByName(parentId, 0));
    }

    private User getUserById(Long userId, int depth) {
        return userRepository.findById(userId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No user with id: " + userId));
    }

    private User getUserByCode(String userCode, int depth) {
        return userRepository.findByCode(userCode.toUpperCase(), depth)
                .orElseThrow(() -> new CustomGraphQLException("No user with code: " + userCode));
    }

    private Group getGroupById(Long groupId, int depth) {
        return groupRepository.findById(groupId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
    }

    private UserDto convertToDto(User user) {
        return UserDto.build(user);
    }

    private List<UserDto> convertToDto(List<User> users) {
        return users.stream()
                .map(UserDto::build)
                .collect(Collectors.toList());
    }


}
