package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public @Data class LiveTestDTO {
    @Id
    @GeneratedValue
    private Long id;;

    private String name;
    private Date creationDate;
    private int maxTimeForTest;
    private int maxTimeForTasks;

    //private GroupDTO groupDTO;//?? KELL-E
    //Ha pl valaki lekér egy livetestDTO-t (findbyid), jöjjön vele GUS és LTUS és Test dto-k is
    //De nemfindbyid-ra hogy a faszomba ott ilyet nem tudsz :(
    //DE  TUDSZ  !!! PATH= (STACKOVERFLOW)
    //TODO

    private TestDTO test;
    //minden?

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    private List<LiveTestUserStateDTO> liveTestUserState;

    public void addLiveTestSate(LiveTestUserStateDTO liveTestUserStateDTO) {
        if (liveTestUserState == null) {
            liveTestUserState = new ArrayList<>();
        }
        liveTestUserState.add(liveTestUserStateDTO);
    }

}
