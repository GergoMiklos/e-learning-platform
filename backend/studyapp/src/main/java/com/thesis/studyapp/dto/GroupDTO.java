package com.thesis.studyapp.dto;

import lombok.Data;

import java.util.List;

public @Data class GroupDTO {

    private Long id;

    private String name;
    private String description;

    private List<UserDTO> users;

    private List<UserDTO> admins;

    private List<LiveTestDTO> liveTests;

    private List<NewsDTO> news;


}
