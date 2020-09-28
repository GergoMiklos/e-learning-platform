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

import java.util.Comparator;

//Todo
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

    private int allSolutions;
    private int correctSolutions;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TASK", direction = Relationship.OUTGOING)
    private Task task;
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TESTTASK", direction = Relationship.INCOMING)
    private Test test;


    public static class TestTaskComparator implements Comparator<TestTask> {

        @Override public int compare(TestTask tt1, TestTask tt2) {
            if (tt1.getLevel() > tt2.getLevel()) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public double getRatio() {
        if (allSolutions != 0) {
            return (double) correctSolutions / (double) allSolutions;
        } else {
            return 0;
        }
    }

}
