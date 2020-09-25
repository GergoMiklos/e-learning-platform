package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.NameDescInput;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final DateUtil dateUtil;

    public Group getGroup(Long groupId) {
        return getGroupById(groupId, 1);
    }

    public List<Group> getGroupsByIds(List<Long> groupIds) {
        return groupRepository.findByIdIn(groupIds, 1);
    }

    @Transactional
    public Group createGroup(Long userId, NameDescInput input) {
        input.validate();
        User user = getUserById(userId, 1);
        Group group = Group.builder()
                .name(input.getName())
                .description(input.getDescription())
                .code(RandomStringUtils.randomAlphanumeric(8).toUpperCase())
                .teachers(Collections.singleton(user))
                .news(user.getName() + " created the group")
                .newsChangedDate(dateUtil.getCurrentTime())
                .build();
        return groupRepository.save(group, 1);
    }

    @Transactional
    public Group editGroup(Long groupId, NameDescInput input) {
        input.validate();
        Group group = getGroupById(groupId, 1);
        group.setName(input.getName());
        group.setDescription(input.getDescription());
        return groupRepository.save(group, 1);
    }

    @Transactional
    public Group changeGroupNews(Long groupId, String news) {
        Group group = getGroupById(groupId, 1);
        group.setNews(news);
        group.setNewsChangedDate(dateUtil.getCurrentTime());
        return groupRepository.save(group, 1);
    }

    //todo userteststatus minden teszthez a groupban
    @Transactional
    public Group addStudentToGroupFromCode(Long studentId, String groupCode) {
        User user = getUserById(studentId, 0);
        Group group = getGroupByCode(groupCode, 1);
        group.addStudent(user);
        return groupRepository.save(group, 1);
    }

    public void deleteStudentFromGroup(Long studentId, Long groupId) {
        if (userRepository.existsById(studentId) && groupRepository.existsById(groupId)) {
            groupRepository.deleteStudent(groupId, studentId);
        } else {
            throw new CustomGraphQLException("No user with id: " + studentId + " or group with id: " + groupId);
        }
    }

    public void deleteTeacherFromGroup(Long teacherId, Long groupId) {
        if (userRepository.existsById(teacherId) && groupRepository.existsById(groupId)) {
            groupRepository.deleteTeacher(groupId, teacherId);
        } else {
            throw new CustomGraphQLException("No user with id: " + teacherId + " or group with id: " + groupId);
        }
    }

    public List<Group> getGroupsForStudent(Long studentId) {
        return groupRepository.findByStudentsIdOrderByName(studentId, 1);
    }

    public List<Group> getGroupsForTeacher(Long studentId) {
        return groupRepository.findByTeachersIdOrderByName(studentId, 1);
    }

    private Group getGroupById(Long groupId, int depth) {
        return groupRepository.findById(groupId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
    }

    private Group getGroupByCode(String groupCode, int depth) {
        return groupRepository.findByCode(groupCode.toUpperCase(), depth)
                .orElseThrow(() -> new CustomGraphQLException("No group with code: " + groupCode));
    }

    //todo nem ide
    private User getUserById(Long userId, int depth) {
        return userRepository.findById(userId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No user with id: " + userId));
    }


}
