package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private String code;
    private String name;
    private String email;
    private String password;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPSTUDENT", direction = Relationship.OUTGOING)
    private List<Group> studentGroups;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPTEACHER", direction = Relationship.OUTGOING)
    private List<Group> teacherGroups;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TASKOWNER", direction = Relationship.INCOMING)
    private List<Task> createdTasks;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "USERSTATUS", direction = Relationship.INCOMING)
    private List<UserTestStatus> userTestStatuses;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "STUDENTPARENT", direction = Relationship.OUTGOING)
    private List<User> followedStudents;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "STUDENTPARENT", direction = Relationship.INCOMING)
    private List<User> parents;

    public void addStudentGroup(Group group) {
        if (studentGroups == null) {
            studentGroups = new ArrayList<>();
        }
        studentGroups.add(group);
    }

    public void addTeacherGroup(Group group) {
        if (teacherGroups == null) {
            teacherGroups = new ArrayList<>();
        }
        teacherGroups.add(group);
    }

    public void addUserTestStatus(UserTestStatus userTestStatus) {
        if (userTestStatuses == null) {
            userTestStatuses = new ArrayList<>();
        }
        userTestStatuses.add(userTestStatus);
    }

    public void addCreatedTask(Task task) {
        if (createdTasks == null) {
            createdTasks = new ArrayList<>();
        }
        createdTasks.add(task);
    }


}
