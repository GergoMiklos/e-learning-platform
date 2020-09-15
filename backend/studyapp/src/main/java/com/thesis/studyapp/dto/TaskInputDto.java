package com.thesis.studyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class TaskInputDto {
    String question;
    String correctAnswer;
    List<String> incorrectAnswers;
}
