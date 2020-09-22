package com.thesis.studyapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskSolutionDto {
    private int chosenAnswerNumber;
    private int solutionNumber;
    private int allAnswers;
    private int correctAnswers;
    private int answeredTasks;
    private int allTasks;
}
