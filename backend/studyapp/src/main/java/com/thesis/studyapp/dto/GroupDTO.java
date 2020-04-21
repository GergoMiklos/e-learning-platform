package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public @Data class GroupDTO {

    private Long id;

    private String name;

    private List<UserDTO> users;

    private List<UserDTO> admins;

    private List<LiveTestDTO> liveTests;


}
