package com.thesis.studyapp.model;

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
    private List<TestTask> tasks;

    @Relationship(type = "TESTGROUP", direction = Relationship.INCOMING)
    private Group group;

    public void addTask(TestTask task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

}
