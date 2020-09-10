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

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestTask {
    @Id
    @GeneratedValue
    private Long id;

    private int level;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "TASK", direction = Relationship.OUTGOING)
    private Task task;

}
