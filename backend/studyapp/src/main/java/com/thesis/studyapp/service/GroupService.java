package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupDto getGroup(Long groupId) {
        return GroupDto.build(getGroupById(groupId, 0));
    }

    public List<GroupDto> getGroupsByIds(List<Long> groupIds) {
        return groupRepository.findByIdIn(groupIds, 0).stream()
                .map(GroupDto::build)
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupDto createGroup(Long userId, String name, String description) {
        User user = getUserById(userId, 0);
        Group group = Group.builder()
                .name(name)
                .description(description)
                .code(RandomStringUtils.random(8).toUpperCase())
                .teachers(Collections.singletonList(user))
                .build();
        return GroupDto.build(groupRepository.save(group));
    }

    @Transactional
    public GroupDto editGroup(Long groupId, String name, String description) {
        Group group = getGroupById(groupId, 0);
        group.setName(name);
        group.setDescription(description);
        return GroupDto.build(groupRepository.save(group));
    }

    @Transactional
    public GroupDto changeGroupNews(Long groupId, String news) {
        Group group = getGroupById(groupId, 0);
        group.setNews(news);
        return GroupDto.build(groupRepository.save(group));
    }

    @Transactional
    public GroupDto addStudentToGroupFromCode(Long studentId, String groupCode) {
        User user = getUserById(studentId, 0);
        Group group = getGroupByCode(groupCode, 1);
        group.getStudents().add(user);
        return GroupDto.build(groupRepository.save(group));
    }

    public void deleteStudentFromGroup(Long userId, Long groupId) {
        //TODO EZEKET A DELETE PATCHEKET lehet jobb lenne csak queryben megírni? vag get group, delete from list, és save?
        groupRepository.deleteStudent(groupId, userId);
    }

    public void deleteTeacherFromGroup(Long userId, Long groupId) {
        //Todo return boolean? NEM, error ha not found
        groupRepository.deleteTeacher(groupId, userId);
    }

    public List<GroupDto> getGroupsForStudent(Long studentId) {
        return groupRepository.findByStudentsIdOrderByName(studentId, 0).stream()
                .map(GroupDto::build)
                .collect(Collectors.toList());
    }

    public List<GroupDto> getGroupsForTeacher(Long studentId) {
        return groupRepository.findByTeachersIdOrderByName(studentId, 0).stream()
                .map(GroupDto::build)
                .collect(Collectors.toList());
    }

    private Group getGroupById(Long groupId, int depth) {
        return groupRepository.findById(groupId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
    }

    private Group getGroupByCode(String groupCode, int depth) {
        return groupRepository.findByCode(groupCode.toUpperCase(), depth)
                .orElseThrow(() -> new CustomGraphQLException("No group with code: " + groupCode));
    }

    private User getUserById(Long userId, int depth) {
        return userRepository.findById(userId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No user with id: " + userId));
    }

    private GroupDto convertUserToDto(Group group) {
        return GroupDto.build(group);
    }

    private List<GroupDto> convertUserToDto(List<Group> groups) {
        return groups.stream()
                .map(GroupDto::build)
                .collect(Collectors.toList());
    }


}
