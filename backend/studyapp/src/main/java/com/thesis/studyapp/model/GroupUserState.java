package com.thesis.studyapp.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

//Ezt az osztályt kiveszem, mert amiért létrehoztam, az avgLevel, mindig a csoportban lévő liveTest->lieTestState-ből fog számolódni
@RelationshipEntity(type = "GROUPUSER")
@Getter @Setter @NoArgsConstructor
public class GroupUserState {
    @Id
    @GeneratedValue
    private Long id;

    private int avgLevel;

    @StartNode
    private Group group;

    @EndNode
    private User user;

}
