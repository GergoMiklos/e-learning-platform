package com.thesis.studyapp.dto;

import com.thesis.studyapp.model.LiveTestUserState;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import lombok.Data;
import org.springframework.data.neo4j.annotation.QueryResult;

import java.util.List;

@QueryResult
public @Data class UserDTO {
    private Long id;

    private String userName;
    private String fullName;

    private List<GroupDTO> groups;
    private List<GroupDTO> managedGroups;
    private List<LiveTestUserStateDTO> liveTestUserStates;
    private List<TestDTO> createdTests;
    private List<TaskDTO> createdTasks;

    private List<Long> groupIds;
    private List<Long> managedGroupIds;
    private List<Long> liveTestUserStateIds;
    private List<Long> createdTestIds;
    private List<Long> createdTaskIds;

}
