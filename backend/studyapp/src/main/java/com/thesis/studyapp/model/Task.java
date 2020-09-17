package com.thesis.studyapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String question;
    //    @Properties
//    @Convert(IntegerMapConverter.class)
//    private Map<Integer, String> answers;
    private int solutionNumber;

    private Set<TaskAnswer> answers = new HashSet<>();
    private Long usage;

    //todo @JsonIgnoreProperties("createdTasks")?
//    @JsonIgnore
//    @EqualsAndHashCode.Exclude
//    @Relationship(type = "TASKOWNER", direction = Relationship.OUTGOING)
//    private User owner;

}
