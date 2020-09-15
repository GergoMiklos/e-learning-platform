package com.thesis.studyapp.model;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
@Data
@Builder
public class TaskAnswer {

    @Id
    @GeneratedValue
    Long id;

    int number;
    String answer;
    //   boolean correct;
}
