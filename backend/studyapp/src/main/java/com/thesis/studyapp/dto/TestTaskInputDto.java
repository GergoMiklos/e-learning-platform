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
public class TestTaskInputDto {
    Long id;

    Integer level;
    String explanation;

    public void validate() {
        if (level != null) {
            if (level < 1 || level > 10) {
                throw new InvalidInputException("Level should be between 1 and 10", "level");
            }
        }

        if (explanation != null) {
            if (explanation.trim().length() < 1 || explanation.length() > 250) {
                throw new InvalidInputException("Explanation should be between 1 and 250 characters", "explanation");
            }
        }
    }
}
