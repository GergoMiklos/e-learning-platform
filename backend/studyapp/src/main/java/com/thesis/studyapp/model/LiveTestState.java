package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.Date;
import java.util.List;

//Todo sok minden
@NodeEntity
@Getter @Setter @NoArgsConstructor
public class LiveTestState {
    @Id
    @GeneratedValue
    private Long id;

    private int currentLevel;
    private int prevAvgLevel;
    private State currentState;
    private int correctsInRow;
    private int failsInRow;

    private Date timeStartedTest;
    private Date timeStartedLastTask;

    List<Long> completedTasksId;
    List<Long> failedTasksId;

    @JsonIgnore
    @Relationship(type="CURRENTTASK", direction = Relationship.OUTGOING)
    Task currentTask;

    @JsonIgnore
    @Relationship(type="USERSTATE", direction = Relationship.INCOMING)
    private User user;

    @JsonIgnore
    @Relationship(type="TESTSTATE", direction = Relationship.INCOMING)
    LiveTest liveTest;


    public enum State {
        NOT_STARTED, IN_PROGRESS, INACTIVE, PROBLEM, FINISHED
    }

}
