package com.thesis.studyapp.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;

@QueryResult
public @Data class NewsDTO {
    private Long id;
    private String text;
    private Date creationDate;
}
