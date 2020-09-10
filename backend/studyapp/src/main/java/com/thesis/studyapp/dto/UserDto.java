package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
@Data
@Builder
public class UserDto implements HasId {

    private Long id;

    private String name;
    private String code;

    public static UserDto build(User user) {
        return UserDto.builder()
                .id(user.getId())
                .code(user.getCode())
                .name(user.getName())
                .build();
    }
}
