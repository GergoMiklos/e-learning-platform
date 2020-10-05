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

import java.time.ZonedDateTime;
import java.util.Comparator;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTestTaskStatus implements HasId, HasRatio {
    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "STATUSDATATASK", direction = Relationship.OUTGOING)
    private TestTask testTask;

    private ZonedDateTime lastSolutionTime;
    private int correctSolutions;
    private int allSolutions;
    private int correctSolutionsInRow;
    private int wrongSolutionsInRow;

    public double getRatio() {
        if (allSolutions != 0) {
            return (double) correctSolutions / (double) allSolutions;
        } else {
            return 0;
        }
    }

}
