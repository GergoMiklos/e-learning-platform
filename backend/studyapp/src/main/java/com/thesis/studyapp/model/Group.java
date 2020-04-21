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

//Nem akarunk mindent mindenkinek kiadni, érdemes úgy alakítani a Rel. Dir-eket, hogy eszerint működjön (User és Admin outgoing, többi incoming)
@NodeEntity
@Getter @Setter @NoArgsConstructor
public class Group {
    @Id
    //@GeneratedValue(strategy = CustomIdStrategy.class)
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @Relationship(type = "GROUPUSER", direction = Relationship.INCOMING)
    private List<User> users;

    @Relationship(type = "GROUPADMIN", direction = Relationship.INCOMING)
    private List<User> admins;

    @Relationship(type = "GROUPLIVETEST", direction = Relationship.OUTGOING)
    private List<LiveTest> liveTests;

    @Relationship(type = "GROUPNEWS", direction = Relationship.OUTGOING)
    private List<News> news;

    public void addUser(User user) {
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

    public void addNews(News newNews) {
        if (news == null) {
            news = new ArrayList<>();
        }
        news.add(newNews);
    }

}
