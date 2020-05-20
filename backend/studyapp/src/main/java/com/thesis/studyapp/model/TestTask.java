package com.thesis.studyapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@NodeEntity
public @Data class TestTask {
    @Id
    @GeneratedValue
    private Long id;

    @Relationship(type = "TASK", direction = Relationship.OUTGOING)
    private int level;

    @EndNode
    private Task task;

}
