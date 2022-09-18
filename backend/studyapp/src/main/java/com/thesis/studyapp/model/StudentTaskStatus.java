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

import java.time.Duration;
import java.time.ZonedDateTime;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentTaskStatus implements HasId, HasRatio {
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "STATUSDATATASK", direction = Relationship.OUTGOING)
    private TestTask testTask;

    private ZonedDateTime lastSolutionTime;
    private Long lastSolutionDurationMs;

    private int correctSolutions;
    private int allSolutions;

    private int correctSolutionsInRow;
    private int wrongSolutionsInRow;

    private int correctSolutionsInCurrentCycle;
    private int allSolutionsInCurrentCycle;

    private int correctSolutionsInPrevCycle;
    private int allSolutionsInPrevCycle;

    public StudentTaskStatus(TestTask testTask, boolean isCorrect, ZonedDateTime solutionTime) {
        this.testTask = testTask;
        setNewSolution(isCorrect, solutionTime);
    }


    public double getRatio() {
        if (allSolutions != 0) {
            return (double) correctSolutions / (double) allSolutions;
        } else {
            return 0;
        }
    }

    public double getRatioInCurrentCycle() {
        if (allSolutionsInCurrentCycle != 0) {
            return (double) correctSolutionsInCurrentCycle / (double) allSolutionsInCurrentCycle;
        } else {
            return 0;
        }
    }

    public double getRatioInPrevCycle() {
        if (allSolutionsInPrevCycle != 0) {
            return (double) correctSolutionsInPrevCycle / (double) correctSolutionsInPrevCycle;
        } else {
            return 0;
        }
    }

    public void setNewSolution(boolean isCorrect, ZonedDateTime newSolutionTime) {
        if (isCorrect) {
            setCorrectSolution();
        } else {
            setWrongSolution();
        }
        if (lastSolutionTime != null) {
            lastSolutionDurationMs = Duration
                    .between(lastSolutionTime.toLocalDateTime(), newSolutionTime.toLocalDateTime()).toMillis();
        }
        lastSolutionTime = newSolutionTime;
    }

    public void setNewCycle() {
        correctSolutionsInPrevCycle = correctSolutionsInCurrentCycle;
        allSolutionsInPrevCycle = allSolutionsInCurrentCycle;

        correctSolutionsInCurrentCycle = 0;
        allSolutionsInCurrentCycle = 0;
    }

    private void setCorrectSolution() {
        correctSolutions = correctSolutions + 1;
        allSolutions = allSolutions + 1;

        correctSolutionsInRow = correctSolutionsInRow + 1;
        wrongSolutionsInRow = 0;

        correctSolutionsInCurrentCycle = correctSolutionsInCurrentCycle + 1;
        allSolutionsInCurrentCycle = allSolutionsInCurrentCycle + 1;
    }

    private void setWrongSolution() {
        allSolutions = allSolutions + 1;

        correctSolutionsInRow = 0;
        wrongSolutionsInRow = wrongSolutionsInRow + 1;

        allSolutionsInCurrentCycle = allSolutionsInCurrentCycle + 1;
    }


}
