package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.List;

public @Data class UserDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String userName;
    private String fullName;
    private String email;

//    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//    //@JsonIgnore
//    private List<GroupUserStateDTO> groups;

    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    //@JsonIgnore
    //private List<GroupDTO> managedGroups;

//    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//    //@JsonIgnore
//    private List<LiveTestUserStateDTO> liveTestUserStates;

}
