package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public @Data class GroupDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<GroupUserStateDTO> users;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<UserDTO> admins;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<LiveTestDTO> liveTests;

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
        if (liveTests == null) {
            liveTests = new ArrayList<>();
        }
        liveTests.add(liveTestDTO);
    }

}
