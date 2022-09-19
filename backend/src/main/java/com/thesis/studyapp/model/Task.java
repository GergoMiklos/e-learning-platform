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
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements HasId {
    @Id
    @GeneratedValue
    private Long id;

    private String question;
    private int solutionNumber;
    private int usage;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TASKANSWER", direction = Relationship.OUTGOING)
    private Set<TaskAnswer> answers = new HashSet<>();

}
