package com.thesis.studyapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class TestTaskStateDTO {
    @Id
    @GeneratedValue
    private Long id;

    private int Level;

//    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//    private TestDTO test;

//    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
//    private TaskDTO task;
    private Long taskId;
    private String taskQuestion;
    private List<String> taskAnswers;
    private int taskSolution;
    private int taskLevel;
    //taskquestion/id/...minden!


}
