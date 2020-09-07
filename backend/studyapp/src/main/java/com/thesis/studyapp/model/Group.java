package com.thesis.studyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NodeEntity
@Getter @Setter @NoArgsConstructor
public class Group {
    @Id
    //@GeneratedValue(strategy = CustomIdStrategy.class)
    @GeneratedValue
    private Long id;

    private String name;
    private String code;
    private String description;

    @Relationship(type = "GROUPUSER", direction = Relationship.INCOMING)
    private List<User> users;

    @Relationship(type = "GROUPADMIN", direction = Relationship.INCOMING)
    private List<User> admins;

    @Relationship(type = "GROUPLIVETEST", direction = Relationship.OUTGOING)
    private List<LiveTest> liveTests;

    @Relationship(type = "TESTGROUP", direction = Relationship.OUTGOING)
    private List<Test> tests;

    @Relationship(type = "GROUPNEWS", direction = Relationship.OUTGOING)
    private News news;

    public void addUser(User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    public void addUsers(List<User> users) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        users.addAll(users);
    }

    public void deleteUser(User user) {
        if(users != null) {
            users = users.stream()
                    .filter(u -> u.getId() != user.getId())
                    .collect(Collectors.toList());
        }
    }

    public void addAdmin(User user) {
        if (admins == null) {
            admins = new ArrayList<>();
        }
        admins.add(user);
    }

    public void deleteAdmin(User user) {
        if(admins != null) {
            admins = admins.stream()
                    .filter(u -> u.getId() != user.getId())
                    .collect(Collectors.toList());
        }
    }

    public void addLiveTest(LiveTest liveTest) {
        if (liveTests == null) {
            liveTests = new ArrayList<>();
        }
        liveTests.add(liveTest);
    }


}
