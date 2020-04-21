package com.thesis.studyapp.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

import java.util.Date;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class LiveTestUserStateDTO {
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

    TaskDTO currentTask;

    //Todo tartalmazás mapping helyett
  //  @JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
  //  private UserDTO user;
    private String userUserName;
    private String userFullName;
    private Long userId;
    //username
    //userfullname
    //userid


    //@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
    //LiveTestDTO liveTest;
    //Itt jó a mapping, mert liveTest tartalmaz???
    private String liveTestName;
    private Long liveTestId;



    public enum State {
        NOT_STARTED, PROBlEM, FINISHED, IN_PROGRESS
    }

}
