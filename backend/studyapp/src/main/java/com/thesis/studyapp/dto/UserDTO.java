package com.thesis.studyapp.dto;

import lombok.Data;

public @Data class UserDTO {
    private Long id;

    private String userName;
    private String fullName;
    private String email;

//    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//    //@JsonIgnore
//    private List<GroupUserStateDTO> groups;

    //@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    //@JsonIgnore
    //private List<GroupDTO> managedGroups;

//    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
//    //@JsonIgnore
//    private List<LiveTestUserStateDTO> liveTestUserStates;

}
