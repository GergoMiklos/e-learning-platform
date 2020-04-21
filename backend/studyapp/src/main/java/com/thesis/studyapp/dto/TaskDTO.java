package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.List;

public @Data class TaskDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String question;
    private List<String> answers;
    private int solution;
    private int level;

}
