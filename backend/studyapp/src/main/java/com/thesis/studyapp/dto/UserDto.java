package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.HasId;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserTestStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;
import java.util.stream.Collectors;

@QueryResult
@Data
@Builder
public class UserDto implements HasId {

    private Long id;

    private String name;
    private String code;

    private List<Long> studentGroupIds;
    private List<Long> teacherGroupIds;
    private List<Long> userTestStatusIds;
    private List<Long> followedStudentIds;
    private List<Long> parentIds;

    public static UserDto build(User user) {
        if (user.getTeacherGroups() == null || user.getStudentGroups() == null || user.getUserTestStatuses() == null
                || user.getFollowedStudents() == null || user.getParents() == null) {
            throw new IllegalStateException("Relationships needed for converting User!");
        }

        return UserDto.builder()
                .id(user.getId())
                .code(user.getCode())
                .name(user.getName())
                .studentGroupIds(user.getStudentGroups().stream()
                        .map(Group::getId).collect(Collectors.toList()))
                .teacherGroupIds(user.getTeacherGroups().stream()
                        .map(Group::getId).collect(Collectors.toList()))
                .userTestStatusIds(user.getUserTestStatuses().stream()
                        .map(UserTestStatus::getId).collect(Collectors.toList()))
                .followedStudentIds(user.getFollowedStudents().stream()
                        .map(User::getId).collect(Collectors.toList()))
                .parentIds(user.getParents().stream()
                        .map(User::getId).collect(Collectors.toList()))
                .build();
    }

    public static List<UserDto> build(List<User> users) {
        return users.stream()
                .map(UserDto::build)
                .collect(Collectors.toList());
    }
}
