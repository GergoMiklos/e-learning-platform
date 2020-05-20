package com.thesis.studyapp.dto;

import com.thesis.studyapp.HasId;
import lombok.Data;
import org.neo4j.ogm.annotation.PostLoad;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@QueryResult
public @Data class GroupDTO implements HasId {

    private Long id;

    private String name;
    private String code;
    private String description;

    private List<Long> userIds;
    private List<Long> adminIds;
    private List<Long> liveTestIds;
    private Long newsId;

//    private List<UserDTO> users;
//    private List<UserDTO> admins;
//    private List<LiveTestDTO> liveTests;
//    private NewsDTO news;

}
