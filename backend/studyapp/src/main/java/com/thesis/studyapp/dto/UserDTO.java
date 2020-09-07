package com.thesis.studyapp.dto;

import com.thesis.studyapp.HasId;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@QueryResult
public @Data class UserDTO implements HasId {

    private Long id;

    private String name;
    private String code;

    private List<Long> groupIds;
    private List<Long> managedGroupIds;
    private List<Long> liveTestStateIds;
    private List<Long> createdTaskIds;

//    private List<GroupDTO> groups;
//    private List<GroupDTO> managedGroups;
//    private List<LiveTestStateDTO> liveTestUserStates;
//    private List<TestDTO> createdTests;
//    private List<TaskDTO> createdTasks;
}
