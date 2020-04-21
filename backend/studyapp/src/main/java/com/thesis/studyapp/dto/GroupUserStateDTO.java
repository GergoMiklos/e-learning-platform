package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;


//Ez már nem kell model szerint
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@Getter @Setter @NoArgsConstructor
public class GroupUserStateDTO {
    @Id
    @GeneratedValue
    private Long id;

    private int avgLevel;

//    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
//    @JsonBackReference
//    private GroupDTO group;
    private String groupName;
    private Long groupId;
    //groupname
    //groupid

//    @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
//    @JsonBackReference
//    private UserDTO user;
    private String userUserName;
    private String userUserFullname;
    private Long userId;
    //username
    //userfullname
    //userid

}
