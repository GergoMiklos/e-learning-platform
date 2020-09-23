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
public class UserTestStatus {
    @Id
    @GeneratedValue
    private Long id;

    private Status status;

    private int currentLevel;
    private int currentCycle;
    private int correctAnswersInRow;
    private int wrongAnswersInRow;
    private int correctAnswers;
    private int allAnswers;
    //    private Date timeStartedTest;
//    private Date startedLastTaskDate;
    private ZonedDateTime statusChangedDate;

//    List<Long> completedTasksId;
//    List<Long> failedTasksId;

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
    private Set<TaskStatus> taskStatuses;

    public void addTaskStatusData(TaskStatus taskStatus) {
        if (this.taskStatuses == null) {
            this.taskStatuses = new HashSet<>();
        }
        this.taskStatuses.add(taskStatus);
    }


    public enum Status {
        NOT_STARTED, IN_PROGRESS, PROBLEM
    }

    @NodeEntity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskStatus {
        @Id
        @GeneratedValue
        private Long id;

        @JsonIgnore
        @EqualsAndHashCode.Exclude
        @Relationship(type = "STATUSDATATASK", direction = Relationship.OUTGOING)
        private TestTask testTask;

        private int correctAnswers;
        private int allAnswers;
        private int correctAnswersInRow;
        private int wrongAnswersInRow;
    }

}
