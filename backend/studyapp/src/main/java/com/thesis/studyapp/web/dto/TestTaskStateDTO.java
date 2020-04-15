package com.thesis.studyapp.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private TestDTO test;

    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    private TaskDTO task;
    //taskquestion/id/...minden!


}
