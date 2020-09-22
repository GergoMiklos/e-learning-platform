package com.thesis.studyapp.exception;

import java.util.Collections;
import java.util.Map;

public class InvalidInputException extends CustomGraphQLException {

    private final String invalidField;

    public InvalidInputException(String message, String invalidField) {
        super(message);
        this.invalidField = invalidField;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.singletonMap("invalidField", invalidField);
    }
}
