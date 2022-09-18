package com.thesis.studyapp.exception;

import java.util.Collections;
import java.util.Map;

public class BadCredentialsException extends CustomGraphQLException {
    public static final String TYPE_NAME = "BAD_CREDENTIALS";

    public BadCredentialsException(String message) {
        super(message);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.singletonMap(TYPE_NAME, getMessage());
    }
}
