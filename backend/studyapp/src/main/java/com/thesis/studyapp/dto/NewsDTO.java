package com.thesis.studyapp.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thesis.studyapp.model.Group;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Date;

@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public @Data class NewsDTO {

    private Long id;

    private String title;
    private String description;
    private Date creationDate;

    //Ez kell hogy kattintható legyen?
    //Vagy Todo groupbynews?
    private String groupName;
    private Long groupId;

}
