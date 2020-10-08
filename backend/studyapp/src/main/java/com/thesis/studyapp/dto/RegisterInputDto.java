package com.thesis.studyapp.dto;

import com.thesis.studyapp.exception.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInputDto {
    private String name;
    private String username;
    private String password;

    public void validate() {
        if (name == null || name.trim().length() < 5 || name.length() > 50) {
            throw new InvalidInputException("Name should be between 5 and 150 characters", "name");
        }
        if (username == null || username.trim().length() < 5 || username.length() > 50) {
            throw new InvalidInputException("Username should be between 5 and 150 characters", "username");
        }
        if (username == null || username.trim().length() < 5 || username.length() > 50) {
            throw new InvalidInputException("Password should be between 5 and 150 characters", "password");
        }
    }
}
