package com.thesis.studyapp.exception;

import java.util.Collections;
import java.util.Map;

public class NotFoundException extends CustomGraphQLException {
    public static final String TYPE_NAME = "NOT_FOUND";

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.singletonMap(TYPE_NAME, getMessage());
    }

}
