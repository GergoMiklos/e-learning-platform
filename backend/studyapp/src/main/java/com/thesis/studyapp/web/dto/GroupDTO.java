package com.thesis.studyapp.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

public @Data class GroupDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private List<GroupUserStateDTO> users;

    private List<UserDTO> admins;

    private List<LiveTestDTO> liveTestDTOS;

    public void addUser(GroupUserStateDTO user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void addAdmin(UserDTO user) {
        if (admins == null) {
            admins = new ArrayList<>();
        }
        admins.add(user);
    }

    public void addLiveTest(LiveTestDTO liveTestDTO) {
        if (liveTestDTOS == null) {
            liveTestDTOS = new ArrayList<>();
        }
        liveTestDTOS.add(liveTestDTO);
    }

}
