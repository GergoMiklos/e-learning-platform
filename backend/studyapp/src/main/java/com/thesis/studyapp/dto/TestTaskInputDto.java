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
public class TestTaskInputDto {
    Long id;
    int level;

    public void validate() {
        if (level < 1 || level > 10) {
            throw new InvalidInputException("Level should be between 1 and 10", "level");
        }
    }
}
