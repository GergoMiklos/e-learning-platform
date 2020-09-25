package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.HasId;
import com.thesis.studyapp.model.Task;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Collections;
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
    private int usage;

    public static TaskDto build(Task task) {
        if (task.getAnswers() == null) {
            throw new IllegalStateException("Answers needed for converting Task!");
        }

        List<TaskAnswerDto> answers = task.getAnswers().stream()
                .map(answer -> new TaskAnswerDto(answer.getNumber(), answer.getAnswer()))
                .collect(Collectors.toList());
        Collections.shuffle(answers);

        return TaskDto.builder()
                .id(task.getId())
                .question(task.getQuestion())
                .answers(answers)
                .solutionNumber(task.getSolutionNumber())
                .usage(task.getUsage())
                .build();
    }

    public static List<TaskDto> build(List<Task> tasks) {
        return tasks.stream()
                .map(TaskDto::build)
                .collect(Collectors.toList());
    }


}
