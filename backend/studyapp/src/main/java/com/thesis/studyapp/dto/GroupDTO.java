package com.thesis.studyapp.dto;

import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

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


}
