package com.thesis.studyapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskAnswerDto {
    int number;
    String answer;
}
