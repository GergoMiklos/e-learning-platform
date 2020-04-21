package com.thesis.studyapp.dto;

import lombok.Data;

import java.util.List;

public @Data class TestDTO {

    private Long id;

    private String name;
    private String description;

    private List<TestTaskStateDTO> tasks;

}
