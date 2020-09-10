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

    //todo dataloader repot vagy servicet használjon?
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

    public List<UserDto> getStudentsForGroup(Long groupId) {
        return userRepository.findByStudentGroupsIdOrderByName(groupId, 0).stream()
                .map(UserDto::build)
                .collect(Collectors.toList());
    }

    public List<UserDto> getTeachersForGroup(Long groupId) {
        return userRepository.findByTeacherGroupsIdOrderByName(groupId, 0).stream()
                .map(UserDto::build)
                .collect(Collectors.toList());
    }

    public List<UserDto> getStudentsForParent(Long parentId) {
        return userRepository.findByParentsIdOrderByName(parentId, 0).stream()
                .map(UserDto::build)
                .collect(Collectors.toList());
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

    private UserDto convertUserToDto(User user) {
        return UserDto.build(user);
    }

    private List<UserDto> convertUserToDto(List<User> users) {
        return users.stream()
                .map(UserDto::build)
                .collect(Collectors.toList());
    }


}
