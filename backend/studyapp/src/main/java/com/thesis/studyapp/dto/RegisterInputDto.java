package com.thesis.studyapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterInputDto {
    private String name;
    private String username;
    private String password;
}
