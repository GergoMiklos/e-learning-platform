package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public @Data class LiveTestDTO {
    private Long id;;

    private String name;
    private Date creationDate;
    private int maxTimeForTest;
    private int maxTimeForTasks;

    private TestDTO test;
    //Todo modelmapper minden, id, name? Szerintem igen

    //TODO LTS kelleni fog, ne LTSbyLTid legyen, hanem ez!
    //De akkor itt null lesz ha grouppal kérdezzük le !!!
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<LiveTestUserStateDTO> liveTestUserState;


}
