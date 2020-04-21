package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Date;
import java.util.List;

@NodeEntity
public @Data class News {
    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String description;
    private Date creationDate;

    @JsonIgnore
    @Relationship(type = "GROUPNEWS", direction = Relationship.INCOMING)
    private Group group;

}
