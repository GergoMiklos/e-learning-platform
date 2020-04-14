package com.thesis.studyapp.web.dto;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

public @Data class TestDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private List<TestTaskStateDTO> tasks;

    public void addTask(TestTaskStateDTO task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

}
