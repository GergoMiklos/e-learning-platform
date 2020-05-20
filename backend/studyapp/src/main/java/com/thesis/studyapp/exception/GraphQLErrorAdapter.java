package com.thesis.studyapp.exception;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.List;
import java.util.Map;

public class GraphQLErrorAdapter implements GraphQLError {

    private final GraphQLError graphQLError;

    public GraphQLErrorAdapter(GraphQLError graphQLError) {
        this.graphQLError = graphQLError;
    }

    @Override
    public List<SourceLocation> getLocations() {
        //return graphQLError.getLocations();
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return graphQLError.getErrorType();
    }

    @Override
    public String getMessage() {
        return graphQLError.getMessage();
    }

    @Override
    public Map<String, Object> getExtensions() {
        return graphQLError.getExtensions();
    }

    @Override
    public List<Object> getPath() {
        return null;
    }

}
