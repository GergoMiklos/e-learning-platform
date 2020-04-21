package com.thesis.studyapp.dto;

import lombok.Data;

import java.util.List;

public @Data class TaskDTO {

    private Long id;

    private String question;
    private List<String> answers;
    private int solution;
    private int level;

}
