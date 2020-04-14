package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public @Data class Group {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @JsonIgnore
    @Relationship(type = "GROUPUSER", direction = Relationship.OUTGOING)
    private List<GroupUserState> users;

    @JsonIgnore
    @Relationship(type = "GROUPADMIN", direction = Relationship.OUTGOING)
    private List<User> admins;

    @JsonIgnore
    @Relationship(type = "GROUPLIVETEST", direction = Relationship.INCOMING)
    private List<LiveTest> liveTests;

    public void addUser(GroupUserState user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void addAdmin(User user) {
        if (admins == null) {
            admins = new ArrayList<>();
        }
        admins.add(user);
    }

    public void addLiveTest(LiveTest liveTest) {
        if (liveTests == null) {
            liveTests = new ArrayList<>();
        }
        liveTests.add(liveTest);
    }

}
