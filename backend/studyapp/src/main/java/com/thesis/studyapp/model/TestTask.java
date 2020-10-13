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

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTask implements HasId, HasRatio {
    @Id
    @GeneratedValue
    private Long id;

    private int level;

    private String explanation;

    private int allSolutions;
    private int correctSolutions;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Relationship(type = "TASK", direction = Relationship.OUTGOING)
    private Task task;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Relationship(type = "TESTTASK", direction = Relationship.INCOMING)
    private Test test;

    public double getRatio() {
        if (allSolutions != 0) {
            return (double) correctSolutions / (double) allSolutions;
        } else {
            return 0;
        }
    }

    public void setNewSolution(boolean isCorrect) {
        if (isCorrect) {
            correctSolutions = correctSolutions + 1;
        }
        allSolutions = allSolutions + 1;
    }

}
