package com.thesis.studyapp.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thesis.studyapp.HasId;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Date;

@QueryResult
public @Data class NewsDTO implements HasId {

    private Long id;

    private String text;
    private Date creationDate;
}
