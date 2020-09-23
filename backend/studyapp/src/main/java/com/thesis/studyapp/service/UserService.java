package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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

    //todo userteststatus minden teszthez a groupban
    @Transactional
    public UserDto addStudentFromCodeToGroup(Long groupId, String studentCode) {
        User user = getUserByCode(studentCode, 1);
        Group group = getGroupById(groupId, 0);
        user.addStudentGroup(group);
        return UserDto.build(userRepository.save(user));
    }

    @Transactional
    public UserDto addTeacherFromCodeToGroup(Long groupId, String teacherCode) {
        User user = getUserByCode(teacherCode, 1);
        Group group = getGroupById(groupId, 0);
        user.addTeacherGroup(group);
        return UserDto.build(userRepository.save(user));
    }

    @Transactional
    public UserDto addStudentFromCodeToParent(Long parentId, String studentCode) {
        User student = getUserByCode(studentCode, 0);
        User parent = getUserById(parentId, 1);
        student.addParent(parent);
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
        //todo check group? NE, ez query (BONTSUK SZÉT ESZERINT? KÜLÖNÁLLÓ QUERY SERVICEK, MUTATIONOK PEDIG EZEKET HASZNÁLJÁK)
        // HISZEN VANNAK KÜLÖNBSÉGEK, ILYENKOR PL NINCS EXCEPTION DOBÁS
        // egy GRAPHQL ALKALMAZÁSBAN SOKK AZ ÖSSZEFÜGGÉS, DE MEGKELL PRÓBÁLNI CSÖKKENTENI AZOKAT7
        // BEVÁLLT MÓDSZER NINCS :(
        return convertToDto(userRepository.findByStudentGroupsIdOrderByName(groupId, 0));
    }

    public List<UserDto> getTeachersForGroup(Long groupId) {
        //todo check group?
        return convertToDto(userRepository.findByTeacherGroupsIdOrderByName(groupId, 0));
    }

    public List<UserDto> getStudentsForParent(Long parentId) {
        //todo check user?
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
