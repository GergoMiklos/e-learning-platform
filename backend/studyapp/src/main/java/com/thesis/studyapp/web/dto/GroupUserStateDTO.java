package com.thesis.studyapp.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

@Getter @Setter @NoArgsConstructor
public class GroupUserStateDTO {
    @Id
    @GeneratedValue
    private Long id;

    private int avgLevel;

    private GroupDTO groupDTO;

    private UserDTO user;



}
