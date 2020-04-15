package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public @Data class TestDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<TestTaskStateDTO> tasks;

    public void addTask(TestTaskStateDTO task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
    }

}
