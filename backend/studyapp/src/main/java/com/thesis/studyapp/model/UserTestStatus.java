package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTestStatus implements HasId, HasRatio {
    @Id
    @GeneratedValue
    private Long id;

    private Status status = Status.NOT_STARTED;
    private ZonedDateTime statusChangedDate;

    private int currentLevel = 1;
    private int currentCycle = 1;

    private int correctSolutionsInRow;
    private int wrongSolutionsInRow;

    private int correctSolutions;
    private int allSolutions;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "CURRENTTESTTASK", direction = Relationship.OUTGOING)
    private TestTask currentTestTask;
    private boolean isCurrentTestTaskSolved;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Relationship(type = "USERSTATUS", direction = Relationship.INCOMING)
    private User user;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Relationship(type = "TESTSTATUS", direction = Relationship.INCOMING)
    private Test test;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TASKSTATUSDATA", direction = Relationship.OUTGOING)
    private Set<UserTestTaskStatus> userTestTaskStatuses = new HashSet<>();


    public void addUserTestTaskStatus(UserTestTaskStatus userTestTaskStatus) {
        if (this.userTestTaskStatuses == null) {
            this.userTestTaskStatuses = new HashSet<>();
        }
        this.userTestTaskStatuses.add(userTestTaskStatus);
    }

    public double getRatio() {
        if (allSolutions != 0) {
            return (double) correctSolutions / (double) allSolutions;
        } else {
            return 0;
        }
    }

    public void setNewCurrentTestTask(TestTask newTestTask) {
        currentTestTask = newTestTask;
        isCurrentTestTaskSolved = false;
    }

    public void increaseCycle() {
        currentCycle = currentCycle + 1;
        currentLevel = 1;
    }

    public void increaseLevel() {
        currentLevel = currentLevel + 1;
        ;
    }

    public void setNewStatus(Status newStatus, ZonedDateTime newStatusChangedDate) {
        status = newStatus;
        statusChangedDate = newStatusChangedDate;
    }

    public void setNewSolution(boolean isCorrect) {
        if (isCorrect) {
            setCorrectSolution();
        } else {
            setWrongSolution();
        }
        isCurrentTestTaskSolved = true;
    }

    //todo setUserTestTaskStatus()?
    private void setCorrectSolution() {
        correctSolutions = correctSolutions + 1;
        allSolutions = allSolutions + 1;

        correctSolutionsInRow = correctSolutionsInRow + 1;
        wrongSolutionsInRow = 0;
    }

    private void setWrongSolution() {
        allSolutions = allSolutions + 1;

        correctSolutionsInRow = 0;
        wrongSolutionsInRow = wrongSolutionsInRow + 1;
    }

    public enum Status {
        NOT_STARTED, IN_PROGRESS, PROBLEM
    }

}
