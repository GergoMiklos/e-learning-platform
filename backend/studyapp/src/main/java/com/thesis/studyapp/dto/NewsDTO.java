package com.thesis.studyapp.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public @Data class NewsDTO {

    private Long id;

    private String title;
    private String description;
    private Date creationDate;

    //Ez kell hogy kattintható legyen?
    //Vagy Todo groupbynews?
    //Szerintem nem kell, ezeket minden lekérdezésnél tudjuk már, LiveTest-nél is
//    private String groupName;
//    private Long groupId;

}
