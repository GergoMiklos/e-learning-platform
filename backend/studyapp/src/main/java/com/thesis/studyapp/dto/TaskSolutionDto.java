package com.thesis.studyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskSolutionDto {
    private int chosenAnswerNumber;
    private int solutionNumber;
    private int allSolutions;
    private int correctSolutions;
    private int solvedTasks;
    private int allTasks;
}
