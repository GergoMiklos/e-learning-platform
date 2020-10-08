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

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements HasId {
    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private String code;
    private String name;

    @Relationship(type = "AUTHDATA", direction = Relationship.OUTGOING)
    private UserAuthData authData;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPSTUDENT", direction = Relationship.OUTGOING)
    private Set<Group> studentGroups = new HashSet<>();
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPTEACHER", direction = Relationship.OUTGOING)
    private Set<Group> teacherGroups = new HashSet<>();
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "USERSTATUS", direction = Relationship.OUTGOING)
    private Set<UserTestStatus> userTestStatuses = new HashSet<>();
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "STUDENTPARENT", direction = Relationship.OUTGOING)
    private Set<User> followedStudents = new HashSet<>();
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "STUDENTPARENT", direction = Relationship.INCOMING)
    private Set<User> parents = new HashSet<>();

    public void addStudentGroup(Group group) {
        if (studentGroups == null) {
            studentGroups = new HashSet<>();
        }
        studentGroups.add(group);
    }

    public void addTeacherGroup(Group group) {
        if (teacherGroups == null) {
            teacherGroups = new HashSet<>();
        }
        teacherGroups.add(group);
    }

    public void addUserTestStatus(UserTestStatus userTestStatus) {
        if (userTestStatuses == null) {
            userTestStatuses = new HashSet<>();
        }
        userTestStatuses.add(userTestStatus);
    }

    public void addParent(User user) {
        if (parents == null) {
            parents = new HashSet<>();
        }
        parents.add(user);
    }

    public void addFollowedStudent(User user) {
        if (followedStudents == null) {
            followedStudents = new HashSet<>();
        }
        followedStudents.add(user);
    }


}
