package com.thesis.studyapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskSolutionDto {
    private int chosenAnswerNumber;
    private int solutionNumber;
    private int allSolutions;
    private int correctSolutions;
    private int solvedTasks;
    private int allTasks;
}
