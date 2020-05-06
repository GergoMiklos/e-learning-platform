package com.thesis.studyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

//Todo egyéb lehetőségek (használjuk ki a rel.ent.-it)
//pl sorszám, vagy bool primary
@RelationshipEntity(type = "TESTTASK")
@Getter @Setter @NoArgsConstructor
public class TestTaskState {
    @Id
    @GeneratedValue
    private Long id;

    private int Level;

    @StartNode
    private Test test;

    @EndNode
    private Task task;

}
