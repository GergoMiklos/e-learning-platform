package com.thesis.studyapp.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public @Data class LiveTestDTO {
    @Id
    @GeneratedValue
    private Long id;;

    private String name;
    private Date creationDate;
    private int maxTimeForTest;
    private int maxTimeForTasks;

    private GroupDTO groupDTO;

    private TestDTO test;

    private List<LiveTestUserStateDTO> liveTestUserStateDTOS;

    public void addLiveTestSate(LiveTestUserStateDTO liveTestUserStateDTO) {
        if (liveTestUserStateDTOS == null) {
            liveTestUserStateDTOS = new ArrayList<>();
        }
        liveTestUserStateDTOS.add(liveTestUserStateDTO);
    }

}
