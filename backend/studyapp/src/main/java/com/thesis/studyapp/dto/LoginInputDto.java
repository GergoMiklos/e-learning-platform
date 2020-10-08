package com.thesis.studyapp.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginInputDto {
    private String username;
    private String password;
}
