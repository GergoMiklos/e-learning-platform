package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public @Data class Test {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @Relationship(type = "TESTTASK", direction = Relationship.OUTGOING)
    private List<Task> tasks;

    @JsonIgnore
    @Relationship(type = "TESTOWNER", direction = Relationship.OUTGOING)
    private User owner;

    public void addTask(Task task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

}
