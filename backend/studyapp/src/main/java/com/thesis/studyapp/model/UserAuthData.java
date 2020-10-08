package com.thesis.studyapp.model;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.id.IdStrategy;
import org.neo4j.ogm.id.UuidStrategy;

import java.util.UUID;

@Data
@Builder
public class UserAuthData {
    @Id
    @GeneratedValue(strategy = UuidStrategy.class)
    private UUID id;
    private String username;
    private String password;
}
