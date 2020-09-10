package com.thesis.studyapp.dto;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

@QueryResult
@Data
public class UserDto implements HasId {

    private Long id;

    private String name;
    private String code;

}
