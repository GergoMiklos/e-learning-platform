package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.NameDescInputDto;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GroupService {
    Group getGroup(Long groupId);

    List<Group> getGroupsForStudent(Long studentId);

    List<Group> getGroupsForTeacher(Long studentId);

    Group createGroup(Long userId, NameDescInputDto input);

    Group editGroup(Long groupId, NameDescInputDto input);

    Group editGroupNews(Long groupId, String news);

    Group addStudentToGroupFromCode(Long studentId, String groupCode);

    User addStudentFromCodeToGroup(Long groupId, String studentCode);

    User addTeacherFromCodeToGroup(Long groupId, String teacherCode);

    void deleteStudentFromGroup(Long studentId, Long groupId);

    void deleteTeacherFromGroup(Long teacherId, Long groupId);

    boolean isTeacherOfGroup(Long userId, Long groupId);
}
