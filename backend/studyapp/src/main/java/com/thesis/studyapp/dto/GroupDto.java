package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.Group;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.time.ZonedDateTime;

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

    public static GroupDto build(Group group) {
        return GroupDto.builder()
                .id(group.getId())
                .code(group.getCode())
                .name(group.getName())
                .description(group.getDescription())
                .news(group.getNews())
                .newsChangedDate(group.getNewsChangedDate())
                .build();
    }
}
