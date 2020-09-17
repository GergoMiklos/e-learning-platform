package com.thesis.studyapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskInputDto {
    String question;
    String correctAnswer;
    List<String> incorrectAnswers;
}
