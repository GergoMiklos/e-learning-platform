package com.thesis.studyapp.web.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thesis.studyapp.model.GroupUserState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@Getter @Setter @NoArgsConstructor
public class GroupUserStateDTO {
    @Id
    @GeneratedValue
    private Long id;

    private int avgLevel;

    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonBackReference
    private GroupDTO group;
    //groupname
    //groupid

    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonBackReference
    private UserDTO user;
    //userusername
    //userfullname
    //userid

}
