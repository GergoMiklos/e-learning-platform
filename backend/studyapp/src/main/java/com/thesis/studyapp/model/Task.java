package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;

@NodeEntity
public @Data class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String question;
    private List<String> answers;
    private int solution;
    private int level;

    @JsonIgnore
    @Relationship(type = "TASKOWNER", direction = Relationship.OUTGOING)
    private User owner;

}
