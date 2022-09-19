package com.thesis.studyapp.exception;

import java.util.Collections;
import java.util.Map;

public class UnauthorizedException extends CustomGraphQLException {
    public static final String TYPE_NAME = "UNAUTHORIZED";

    public UnauthorizedException(String message) {
        super(message);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.singletonMap(TYPE_NAME, getMessage());
    }

}
