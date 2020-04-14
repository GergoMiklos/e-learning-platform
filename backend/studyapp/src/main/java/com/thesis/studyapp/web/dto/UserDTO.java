package com.thesis.studyapp.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

public @Data class UserDTO {
    @Id
    @GeneratedValue
    private Long id;

    private String userName;
    private String fullName;
    private String email;

    private List<GroupUserStateDTO> groups;

    private List<GroupDTO> managedGroupDTOS;

    private List<LiveTestUserStateDTO> liveTestUserStateDTOS;

    public void addGroup(GroupUserStateDTO group) {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        groups.add(group);
    }

    public void addManagedGroup(GroupDTO groupDTO) {
        if (managedGroupDTOS == null) {
            managedGroupDTOS = new ArrayList<>();
        }
        managedGroupDTOS.add(groupDTO);
    }

    public void addLiveTestState(LiveTestUserStateDTO liveTestUserStateDTO) {
        if (liveTestUserStateDTOS == null) {
            liveTestUserStateDTOS = new ArrayList<>();
        }
        liveTestUserStateDTOS.add(liveTestUserStateDTO);
    }


}
