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

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

//Todo sok minden
@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTestStatus implements HasId, HasRatio {
    @Id
    @GeneratedValue
    private Long id;

    private Status status;
    private int currentLevel;
    private int currentCycle;
    private int correctSolutionsInRow;
    private int wrongSolutionsInRow;
    private int correctSolutions;
    private int allSolutions;

    private ZonedDateTime statusChangedDate;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "CURRENTTESTTASK", direction = Relationship.OUTGOING)
    private TestTask currentTestTask;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "USERSTATUS", direction = Relationship.INCOMING)
    private User user;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TESTSTATUS", direction = Relationship.INCOMING)
    private Test test;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TASKSTATUSDATA", direction = Relationship.OUTGOING)
    private Set<UserTestTaskStatus> userTestTaskStatuses = new HashSet<>();

    public void addUserTestTaskStatus(UserTestTaskStatus userTestTaskStatus) {
        if (this.userTestTaskStatuses == null) {
            this.userTestTaskStatuses = new HashSet<>();
        }
        this.userTestTaskStatuses.add(userTestTaskStatus);
    }

    public double getRatio() {
        if (allSolutions != 0) {
            return (double) correctSolutions / (double) allSolutions;
        } else {
            return 0;
        }
    }

    public enum Status {
        NOT_STARTED, IN_PROGRESS, PROBLEM
    }

}
