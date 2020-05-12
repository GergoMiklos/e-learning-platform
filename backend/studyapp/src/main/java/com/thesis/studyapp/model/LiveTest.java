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
import java.util.Date;
import java.util.List;


@NodeEntity
@Getter @Setter @NoArgsConstructor
public class LiveTest {
    @Id
    @GeneratedValue
    private Long id;

    private Date creationDate;
    //TODO time-okat test-nél?
    private int maxTimeForTest;
    private int maxTimeForTasks;

    @JsonIgnore
    @Relationship(type = "GROUPLIVETEST", direction = Relationship.INCOMING)
    private Group group;

    @JsonIgnore
    @Relationship(type = "LIVETESTTEST", direction = Relationship.OUTGOING)
    private Test test;

    @JsonIgnore
    @Relationship(type = "TESTSTATE", direction = Relationship.OUTGOING)
    private List<LiveTestState> liveTestStates;


    public void addLiveTestSate(LiveTestState liveTestState) {
        if (liveTestStates == null) {
            liveTestStates = new ArrayList<>();
        }
        liveTestStates.add(liveTestState);
    }

}
