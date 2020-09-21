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

import java.time.LocalDateTime;
import java.util.Date;

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

    private int correctAnswersInRow;
    private int wrongAnswersInRow;
    private int correctAnswers;
    private int allAnswers;
    //    private Date timeStartedTest;
//    private Date startedLastTaskDate;
    private LocalDateTime statusChangedDate;

//    List<Long> completedTasksId;
//    List<Long> failedTasksId;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "CURRENTTASK", direction = Relationship.OUTGOING)
    Task currentTask;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "USERSTATUS", direction = Relationship.INCOMING)
    private User user;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TESTSTATUS", direction = Relationship.INCOMING)
    Test test;

    public enum Status {
        NOT_STARTED, IN_PROGRESS, PROBLEM, FINISHED
    }

}
