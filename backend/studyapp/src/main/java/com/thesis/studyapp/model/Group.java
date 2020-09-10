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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    //@GeneratedValue(strategy = CustomIdStrategy.class)
    @GeneratedValue
    private Long id;

    private String name;
    private String code;
    private String description;
    private String news;
    private Date newsChangedDate;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPSTUDENT", direction = Relationship.INCOMING)
    private List<User> students;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPTEACHER", direction = Relationship.INCOMING)
    private List<User> teachers;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPTEST", direction = Relationship.OUTGOING)
    private List<Test> tests;

    public void addUser(User user) {
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(user);
    }

    public void addAdmin(User user) {
        if (teachers == null) {
            teachers = new ArrayList<>();
        }
        teachers.add(user);
    }

    public void addTest(Test test) {
        if (tests == null) {
            tests = new ArrayList<>();
        }
        tests.add(test);
    }

}
