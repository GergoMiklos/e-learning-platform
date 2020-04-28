package com.thesis.studyapp.dto;

import lombok.Data;
import org.neo4j.ogm.annotation.PostLoad;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@QueryResult
public @Data class GroupDTO {

    private Long id;

    private String name;
    private String description;

    private List<UserDTO> users;

    private List<UserDTO> admins;

    private List<LiveTestDTO> liveTests;

    private List<NewsDTO> news;

    private List<Long> userIds;
    private List<Long> adminIds;



}
