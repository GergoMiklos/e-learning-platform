package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
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
import java.util.UUID;


@NodeEntity
@Getter @Setter @NoArgsConstructor
public class LiveTest {
    @Id
    @GeneratedValue
    private Long id;
    private String uuid;

    private String name;
    private Date creationDate;
    private int maxTimeForTest;
    private int maxTimeForTasks;

    @JsonIgnore
    @Relationship(type = "GROUPLIVETEST", direction = Relationship.INCOMING)
    private Group group;

    @JsonIgnore
    @Relationship(type = "LIVETESTTEST", direction = Relationship.OUTGOING)
    private Test test;

    @JsonIgnore
    @Relationship(type = "LIVETESTUSER", direction = Relationship.OUTGOING)
    private List<LiveTestUserState> liveTestUserStates;


    public void addLiveTestSate(LiveTestUserState liveTestUserState) {
        if (liveTestUserStates == null) {
            liveTestUserStates = new ArrayList<>();
        }
        liveTestUserStates.add(liveTestUserState);
    }

}
