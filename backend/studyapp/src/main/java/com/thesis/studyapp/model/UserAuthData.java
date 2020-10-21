package com.thesis.studyapp.model;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

@Data
@Builder
public class UserAuthData {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
}
