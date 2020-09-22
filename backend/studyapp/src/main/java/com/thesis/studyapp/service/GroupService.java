package com.thesis.studyapp.service;

import com.thesis.studyapp.configuration.DateUtil;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final DateUtil dateUtil;

    public GroupDto getGroup(Long groupId) {
        return convertToDto(getGroupById(groupId, 0));
    }

    public List<GroupDto> getGroupsByIds(List<Long> groupIds) {
        return convertToDto(groupRepository.findByIdIn(groupIds, 0));
    }

    @Transactional
    public GroupDto createGroup(Long userId, NameDescInputDto input) {
        input.validate();
        User user = getUserById(userId, 0);
        Group group = Group.builder()
                .name(input.getName())
                .description(input.getDescription())
                .code(RandomStringUtils.randomAlphanumeric(8).toUpperCase())
                .teachers(Collections.singleton(user))
                .news(user.getName() + " created the group")
                .newsChangedDate(dateUtil.getCurrentTime())
                .build();
        return convertToDto(groupRepository.save(group));
    }

    @Transactional
    public GroupDto editGroup(Long groupId, NameDescInputDto input) {
        input.validate();
        Group group = getGroupById(groupId, 0);
        group.setName(input.getName());
        group.setDescription(input.getDescription());
        return convertToDto(groupRepository.save(group));
    }

    @Transactional
    public GroupDto changeGroupNews(Long groupId, String news) {
        Group group = getGroupById(groupId, 0);
        group.setNews(news);
        group.setNewsChangedDate(dateUtil.getCurrentTime());
        return convertToDto(groupRepository.save(group));
    }

    //todo userteststatus minden teszthez a groupban
    @Transactional
    public GroupDto addStudentToGroupFromCode(Long studentId, String groupCode) {
        User user = getUserById(studentId, 0);
        Group group = getGroupByCode(groupCode, 1);
        group.addStudent(user);
        return convertToDto(groupRepository.save(group));
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

    public List<GroupDto> getGroupsForStudent(Long studentId) {
        return convertToDto(groupRepository.findByStudentsIdOrderByName(studentId, 0));
    }

    public List<GroupDto> getGroupsForTeacher(Long studentId) {
        return convertToDto(groupRepository.findByTeachersIdOrderByName(studentId, 0));
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

    private GroupDto convertToDto(Group group) {
        return GroupDto.build(group);
    }

    private List<GroupDto> convertToDto(List<Group> groups) {
        return groups.stream()
                .map(GroupDto::build)
                .collect(Collectors.toList());
    }


}
