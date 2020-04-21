package com.thesis.studyapp.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

public @Data class LiveTestDTO {
    private Long id;;

    private String name;
    private String description;
    private Date creationDate;
    private int maxTimeForTest;
    private int maxTimeForTasks;

    //private TestDTO test;
    private String testId;
    private String testName;
    //Todo modelmapper minden, id, name? Szerintem igen

    //TODO LTS kelleni fog, ne LTSbyLTid legyen, hanem ez!
    //De akkor itt null lesz ha grouppal kérdezzük le !!!? Leszarom
    private List<LiveTestUserStateDTO> liveTestUserState;


}
