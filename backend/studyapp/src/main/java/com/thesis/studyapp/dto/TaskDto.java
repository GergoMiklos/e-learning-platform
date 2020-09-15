package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.Task;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;
import java.util.stream.Collectors;

@QueryResult
@Data
@Builder
public class TaskDto implements HasId {

    private Long id;

    private String question;
    private List<TaskAnswerDto> answers;
    private int solutionNumber;

    public static TaskDto build(Task task) {
        if (task.getAnswers() == null) {
            throw new IllegalStateException("Answers needed for converting Task!");
        }
        ;
        return TaskDto.builder()
                .id(task.getId())
                .question(task.getQuestion())
//                .answers(task.getAnswers().keySet().stream()
//                        .map(number -> new TaskAnswerDto(number, task.getAnswers().get(number)))
//                        .collect(Collectors.toList()))
                .answers(task.getAnswers().stream()
                        .map(answer -> new TaskAnswerDto(answer.getNumber(), answer.getAnswer()))
                        .collect(Collectors.toList()))
                .solutionNumber(task.getSolutionNumber())
                .build();
    }

}
