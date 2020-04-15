package com.thesis.studyapp.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public @Data class UserDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String userName;
    private String fullName;
    private String email;

    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    //@JsonIgnore
    //private List<GroupUserStateDTO> groups;

    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    //@JsonIgnore
    //private List<GroupDTO> managedGroups;

    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    //@JsonIgnore
    //private List<LiveTestUserStateDTO> liveTestUserStates;

//    public void addGroup(GroupUserStateDTO group) {
//        if (groups == null) {
//            groups = new ArrayList<>();
//        }
//        groups.add(group);
//    }

//    public void addManagedGroup(GroupDTO groupDTO) {
//        if (managedGroups == null) {
//            managedGroups = new ArrayList<>();
//        }
//        managedGroups.add(groupDTO);
//    }

//    public void addLiveTestState(LiveTestUserStateDTO liveTestUserStateDTO) {
//        if (liveTestUserStates == null) {
//            liveTestUserStates = new ArrayList<>();
//        }
//        liveTestUserStates.add(liveTestUserStateDTO);
//    }


}
