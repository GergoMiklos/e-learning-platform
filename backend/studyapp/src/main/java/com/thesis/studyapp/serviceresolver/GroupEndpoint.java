package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.GroupDto;
import com.thesis.studyapp.dto.UserDto;
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
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public Optional<GroupDto> getGroup(Long id) {
        return groupRepository.getById(id);
    }

    public List<GroupDto> getGroupsByIds(List<Long> groupIds) {
        return groupRepository.getByIds(groupIds);
    }

    @Transactional
    public Optional<GroupDto> createGroup(Long userId, String name, String description) {
        User user = userRepository.findById(userId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No user with id: " + userId));
        Group group = Group.builder()
                .name(name)
                .description(description)
                .code(RandomStringUtils.random(8).toUpperCase())
                .teachers(Collections.singletonList(user))
                .build();

        group = groupRepository.save(group);
        return getGroup(group.getId());
    }

    @Transactional
    public Optional<GroupDto> editGroup(Long groupId, String name, String description) {
        Group group = groupRepository.findById(groupId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
        group.setName(name);
        group.setDescription(description);
        group = groupRepository.save(group);
        return getGroup(group.getId());
    }

    @Transactional
    public Optional<GroupDto> changeGroupNews(Long groupId, String news) {
        Group group = groupRepository.findById(groupId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
        group.setNews(news);
        groupRepository.save(group);
        return getGroup(group.getId());
    }

    @Transactional
    public Optional<UserDto> addStudentFromCodeToGroup(Long groupId, String userCode) {
        //Todo
        //TODO itt leirom mit kéne csinálni, ez a GroupEndpoint.
        //Mindent bonts fel a lehető legkisebb függvényekbe, pl. itt ne groupRepot hívj, hanem egy másik függvény ezt már megcsilta,
        //vagy épp  ne userRepot
        //Mindent a tesztnek !!!

        Group group = groupRepository.findById(groupId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
        Optional<User> studentFromCode = userRepository.findByCode(userCode.toUpperCase());
        return studentFromCode.map(user -> {
            group.getStudents().add(user);
            groupRepository.save(group);
            return userRepository.getById(user.getId());
        }).orElseThrow(() -> new CustomGraphQLException("No user with code: " + userCode));
    }

    @Transactional
    public Optional<UserDto> addTeacherFromCodeToGroup(Long groupId, String userCode) {
        Group group = groupRepository.findById(groupId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
        Optional<User> teacherFromCode = userRepository.findByCode(userCode.toUpperCase());
        return teacherFromCode.map(user -> {
            group.getTeachers().add(user);
            groupRepository.save(group);
            return userRepository.getById(user.getId());
        }).orElseThrow(() -> new CustomGraphQLException("No user with code: " + userCode));
    }

    @Transactional
    public Optional<GroupDto> addStudentToGroupFromCode(Long userId, String groupCode) {
        User user = userRepository.findById(userId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No user with id: " + userId));
        Optional<Group> groupFromCode = groupRepository.findByCode(groupCode.toUpperCase());
        return groupFromCode.map(group -> {
            group.getStudents().add(user);
            groupRepository.save(group);
            //Todo return convertToDto(group)?
            return getGroup(group.getId());
        }).orElseThrow(() -> new CustomGraphQLException("No group with code: " + groupCode));
    }

    public void deleteStudentFromGroup(Long userId, Long groupId) {
        //TODO EZEKET A DELETE PATCHEKET lehet jobb lenne csak queryben megírni? vag get group, delete from list, és save?
        groupRepository.deleteStudent(groupId, userId);
    }

    public void deleteTeacherFromGroup(Long userId, Long groupId) {
        //Todo return boolean? NEM, error ha not found
        groupRepository.deleteTeacher(groupId, userId);
    }

}
