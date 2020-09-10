package com.thesis.studyapp.dto;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;

@QueryResult
@Data
public class GroupDto implements HasId {

    private Long id;

    private String code;
    private String name;
    private String description;
    private String news;
    private Date newsChangedDate;

}
