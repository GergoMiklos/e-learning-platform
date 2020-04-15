package com.thesis.studyapp.model;

import org.neo4j.ogm.id.IdStrategy;

import java.util.UUID;

public class CustomIdStrategy implements IdStrategy {
    @Override
    public Object generateId(Object entity) {
        return (UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    }
}
