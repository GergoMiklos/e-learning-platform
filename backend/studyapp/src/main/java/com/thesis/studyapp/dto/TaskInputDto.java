package com.thesis.studyapp.dto;

import com.thesis.studyapp.exception.InvalidInputException;
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

    public void validate() {
        if (question == null || question.trim().length() < 1 || question.length() > 250) {
            throw new InvalidInputException("Question should be between 1 and 250 characters", "question");
        }
        if (correctAnswer == null || correctAnswer.trim().length() < 1 || correctAnswer.length() > 250) {
            throw new InvalidInputException("Answers should be between 1 and 250 characters", "correctAnswer");
        }
        incorrectAnswers.forEach(answer -> {
            if (answer == null || answer.trim().length() < 1 || answer.length() > 250) {
                throw new InvalidInputException("Answers should be between 1 and 250 characters", "incorrectAnswers");
            }
        });

    }
}
