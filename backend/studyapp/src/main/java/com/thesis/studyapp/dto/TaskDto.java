package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.Task;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@QueryResult
@Data
@Builder
public class TaskDto implements HasId {

    private Long id;

    private String question;
    private List<String> answers;
    private int solution;

    public static TaskDto build(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .question(task.getQuestion())
                .answers(task.getAnswers())
                .solution(task.getSolution())
                .build();
    }

}
