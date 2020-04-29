package com.thesis.studyapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

//TODO EZ AZ OSZTÁLY NEM KELL, KÜLÖNBSÉG CSAK REPOBOL
@QueryResult
@Getter @Setter @NoArgsConstructor
public class TestTaskStateDTO {
    private Long id;

    private int Level;

//    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//    private TestDTO test;

    //rivate TaskDTO task;
    //TODO szerintem inkább task tartalmazás?
    //hamár úgyis rel., jön vele a depth-szel
    private Long taskId;
    private String question;
    private List<String> answers;
    private int solution;
    //private int level;


}
