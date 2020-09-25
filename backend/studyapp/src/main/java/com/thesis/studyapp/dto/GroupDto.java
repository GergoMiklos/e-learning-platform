package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.HasId;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@QueryResult
@Data
@Builder
public class GroupDto implements HasId {

    private Long id;

    private String code;
    private String name;
    private String description;
    private String news;
    private ZonedDateTime newsChangedDate;

    private List<Long> studentIds;
    private List<Long> teacherIds;
    private List<Long> testIds;

    public static GroupDto build(Group group) {
        if (group.getTests() == null || group.getStudents() == null || group.getTeachers() == null) {
            throw new IllegalStateException("Relationships needed for converting Group!");
        }
        return GroupDto.builder()
                .id(group.getId())
                .code(group.getCode())
                .name(group.getName())
                .description(group.getDescription())
                .news(group.getNews())
                .newsChangedDate(group.getNewsChangedDate())
                .testIds(group.getTests().stream()
                        .map(Test::getId).collect(Collectors.toList()))
                .studentIds(group.getStudents().stream()
                        .map(User::getId).collect(Collectors.toList()))
                .teacherIds(group.getTeachers().stream()
                        .map(User::getId).collect(Collectors.toList()))
                .build();
    }

    public static List<GroupDto> build(List<Group> groups) {
        return groups.stream()
                .map(GroupDto::build)
                .collect(Collectors.toList());
    }
}
