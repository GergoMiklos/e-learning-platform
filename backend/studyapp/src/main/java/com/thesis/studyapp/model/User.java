package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
@Getter @Setter @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String code;
    private String email;
    private String password;

    @JsonIgnore
    @Relationship(type = "GROUPUSER", direction = Relationship.OUTGOING)
    private List<Group> groups;

    @JsonIgnore
    @Relationship(type = "GROUPADMIN", direction = Relationship.OUTGOING)
    private List<Group> managedGroups;

    @JsonIgnore
    @Relationship(type = "TASKOWNER", direction = Relationship.INCOMING)
    private List<Task> createdTasks;

    @JsonIgnore
    @Relationship(type = "USERSTATE", direction = Relationship.INCOMING)
    private List<LiveTestState> liveTestStates;

    public void addGroup(Group group) {
        if (groups == null) {
            groups = new ArrayList<>();
        }
        groups.add(group);
    }

    public void addManagedGroup(Group group) {
        if (managedGroups == null) {
            managedGroups = new ArrayList<>();
        }
        managedGroups.add(group);
    }

    public void addLiveTestState(LiveTestState liveTestState) {
        if (liveTestStates == null) {
            liveTestStates = new ArrayList<>();
        }
        liveTestStates.add(liveTestState);
    }

    public void addCreatedTask(Task task) {
        if (createdTasks == null) {
            createdTasks = new ArrayList<>();
        }
        createdTasks.add(task);
    }


}
