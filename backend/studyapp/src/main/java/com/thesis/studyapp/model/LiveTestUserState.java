package com.thesis.studyapp.model;

import lombok.*;
import org.neo4j.ogm.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.ListResourceBundle;

//Todo sok minden
@RelationshipEntity(type = "LIVETESTUSER")
@Getter @Setter @NoArgsConstructor
public class LiveTestUserState {
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

    @EndNode
    private User user;

    @StartNode
    LiveTest liveTest;


    public enum State {
        NOT_STARTED, PROBlEM, FINISHED, IN_PROGRESS
    }

}
