package com.thesis.studyapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.*;

//Sosem használt osztály, arra lett volna hivatott, hogy különböző jogokat adhassunk
//(Ez külön List-tel egyébként kiválható ha kell, de ez is egy megoldás, mapperrel)
@RelationshipEntity(type = "GROUPUSER")
@Getter
@Setter
@NoArgsConstructor
public class GroupAdminState {
    @Id
    @GeneratedValue
    private Long id;

    private Roles role;

    @StartNode
    private Group group;

    @EndNode
    private User user;

    public enum Roles { //Todo néző (szülő), feladat rétrehozó
        ADMIN
    }

}
