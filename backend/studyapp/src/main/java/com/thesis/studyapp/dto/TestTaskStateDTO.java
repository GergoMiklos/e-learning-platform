package com.thesis.studyapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class TestTaskStateDTO {
    private Long id;

    private int Level;

//    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//    private TestDTO test;

    private TaskDTO task;
    //TODO szerintem inkább task tartalmazás?
    //hamár úgyis rel., jön vele a depth-szel
//    private Long taskId;
//    private String taskQuestion;
//    private List<String> taskAnswers;
//    private int taskSolution;
//    private int taskLevel;
    //taskquestion/id/...minden!


}
