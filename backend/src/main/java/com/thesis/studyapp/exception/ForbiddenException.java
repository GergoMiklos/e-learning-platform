package com.thesis.studyapp.exception;

import java.util.Collections;
import java.util.Map;

public class ForbiddenException extends CustomGraphQLException {
    public static final String TYPE_NAME = "FORBIDDEN";

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.singletonMap(TYPE_NAME, getMessage());
    }
}
