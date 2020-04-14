package com.thesis.studyapp.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@Getter @Setter @NoArgsConstructor
public class TestTaskStateDTO {
    @Id
    @GeneratedValue
    private Long id;

    private int Level;

    private TestDTO test;

    private TaskDTO taskDTO;

    //private Task task;

}
