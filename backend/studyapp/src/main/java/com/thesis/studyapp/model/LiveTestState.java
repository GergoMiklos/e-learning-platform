package com.thesis.studyapp.model;

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

    @Relationship(type="CURRENTTASK", direction = Relationship.OUTGOING)
    Task currentTask;

    @Relationship(type="USERSTATE", direction = Relationship.INCOMING)
    private User user;

    @Relationship(type="TESTSTATE", direction = Relationship.INCOMING)
    LiveTest liveTest;


    public enum State {
        NOT_STARTED, PROBlEM, FINISHED, IN_PROGRESS
    }

}
