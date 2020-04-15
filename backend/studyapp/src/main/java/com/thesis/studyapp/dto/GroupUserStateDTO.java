package com.thesis.studyapp.dto;

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
    //userusername
    //userfullname
    //userid

}
