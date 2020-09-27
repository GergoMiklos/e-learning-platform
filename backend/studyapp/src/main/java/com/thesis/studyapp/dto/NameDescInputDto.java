package com.thesis.studyapp.dto;

import com.thesis.studyapp.exception.InvalidInputException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NameDescInputDto {
    String name;
    String description;

    public void validate() {
        if (name == null || name.trim().length() < 5 || name.length() > 150) {
            throw new InvalidInputException("Name should be between 5 and 150 characters", "name");
        }
        if (description == null || description.trim().length() < 5 || description.length() > 500) {
            throw new InvalidInputException("Description should be between 5 and 500 characters", "description");
        }
    }
}
