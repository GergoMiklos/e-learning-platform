package com.thesis.studyapp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthData {
    private String username;
    private String password;
}
