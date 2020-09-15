package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPTEST", direction = Relationship.INCOMING)
    private Group group;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TESTTASK", direction = Relationship.OUTGOING)
    private List<TestTask> testTasks;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TESTSTATUS", direction = Relationship.OUTGOING)
    private List<UserTestStatus> userTestStatuses;

    public void addUserTestStatus(UserTestStatus userTestStatus) {
        if (userTestStatuses == null) {
            userTestStatuses = new ArrayList<>();
        }
        userTestStatuses.add(userTestStatus);
    }

    public void addTask(TestTask task) {
        if (testTasks == null) {
            testTasks = new ArrayList<>();
        }
        testTasks.add(task);
    }

    public enum Status {
        OFFLINE, ONLINE
    }

}
