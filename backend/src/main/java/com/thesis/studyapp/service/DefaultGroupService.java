package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.exception.ForbiddenException;
import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import com.thesis.studyapp.util.AuthenticationUtil;
import com.thesis.studyapp.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DefaultGroupService implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    private final AuthenticationUtil authenticationUtil;
    private final DateUtil dateUtil;

    @Override public Group getGroup(Long groupId) {
        return getGroupById(groupId, 1);
    }

    @Override public List<Group> getGroupsForStudent(Long studentId) {
        return groupRepository.findByStudentsIdOrderByName(studentId, 1);
    }

    @Override public List<Group> getGroupsForTeacher(Long studentId) {
        return groupRepository.findByTeachersIdOrderByName(studentId, 1);
    }

    @Override @Transactional
    public Group createGroup(Long userId, NameDescInputDto input) {
        input.validate();

        User user = getUserById(userId);

        Group group = Group.builder()
                .name(input.getName())
                .description(input.getDescription())
                .code(createGroupCode())
                .teachers(Set.of(user))
                .news(user.getName())
                .newsChangedDate(dateUtil.getCurrentTime())
                .build();
        return groupRepository.save(group, 1);
    }

    @Override @Transactional
    public Group editGroup(Long groupId, NameDescInputDto input) {
        input.validate();

        Group group = getGroupById(groupId, 1);
        validateTeacherRole(group);

        group.setName(input.getName());
        group.setDescription(input.getDescription());

        return groupRepository.save(group, 1);
    }

    @Override @Transactional
    public Group editGroupNews(Long groupId, String news) {
        Group group = getGroupById(groupId, 1);
        validateTeacherRole(group);

        group.setNews(news);
        group.setNewsChangedDate(dateUtil.getCurrentTime());

        return groupRepository.save(group, 1);
    }

    @Override @Transactional
    public Group addStudentToGroupFromCode(Long studentId, String groupCode) {
        Group group = getGroupByCode(groupCode, 1);
        User user = getUserById(studentId);

        group.addStudent(user);
        return groupRepository.save(group, 1);
    }

    @Override @Transactional
    public User addStudentFromCodeToGroup(Long groupId, String studentCode) {
        User student = getUserByCode(studentCode);
        Group group = getGroupById(groupId, 0);

        student.addStudentGroup(group);
        return userRepository.save(student, 1);
    }

    @Override @Transactional
    public User addTeacherFromCodeToGroup(Long groupId, String teacherCode) {
        User teacher = getUserByCode(teacherCode);
        Group group = getGroupById(groupId, 1);
        validateTeacherRole(group);

        teacher.addTeacherGroup(group);
        return userRepository.save(teacher, 1);
    }

    @Override @Transactional
    public void deleteStudentFromGroup(Long studentId, Long groupId) {
        if (userRepository.existsById(studentId) && groupRepository.existsById(groupId)) {
            groupRepository.deleteStudent(groupId, studentId);
        } else {
            throw new NotFoundException("No user with id: " + studentId + " or group with id: " + groupId);
        }
    }

    @Override @Transactional
    public void deleteTeacherFromGroup(Long teacherId, Long groupId) {
        Group group = getGroupById(groupId, 1);
        validateTeacherRole(group);

        if (userRepository.existsById(teacherId)) {
            groupRepository.deleteTeacher(groupId, teacherId);
        } else {
            throw new NotFoundException("No user with id: " + teacherId + " or group with id: " + groupId);
        }
    }

    @Override public boolean isTeacherOfGroup(Long userId, Long groupId) {
        return getGroupById(groupId, 1).getTeachers().stream()
                .anyMatch(teacher -> teacher.getId().equals(userId));
    }

    private void validateTeacherRole(Group group) {
        Long requesterId = authenticationUtil.getPrincipals().getUserId();
        if (group.getTeachers().stream().noneMatch(teacher -> teacher.getId().equals(requesterId))) {
            throw new ForbiddenException("This request authorized only for teachers");
        }
    }

    private Group getGroupById(Long groupId, int depth) {
        return groupRepository.findById(groupId, depth)
                .orElseThrow(() -> new NotFoundException("No group with id: " + groupId));
    }

    private Group getGroupByCode(String groupCode, int depth) {
        return groupRepository.findByCodeIgnoreCase(groupCode.toUpperCase(), depth)
                .orElseThrow(() -> new NotFoundException("No group with code: " + groupCode));
    }

    private String createGroupCode() {
        String code;
        do {
            code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        } while (groupRepository.existsByCodeIgnoreCase(code));
        return code;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId, 1)
                .orElseThrow(() -> new NotFoundException("No user with id: " + userId));
    }

    private User getUserByCode(String userCode) {
        return userRepository.findByCodeIgnoreCase(userCode, 1)
                .orElseThrow(() -> new NotFoundException("No user with code: " + userCode));
    }

}
