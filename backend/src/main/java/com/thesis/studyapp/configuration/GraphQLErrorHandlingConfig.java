package com.thesis.studyapp.configuration;

import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.exception.GraphQLErrorAdapter;
import graphql.ExceptionWhileDataFetching;
import graphql.GraphQLError;
import graphql.servlet.DefaultGraphQLErrorHandler;
import graphql.servlet.GenericGraphQLError;
import graphql.servlet.GraphQLErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class GraphQLErrorHandlingConfig {

    /**
     * Changing default GraphQLErrorHandler behaviour to show
     * default client errors and
     * filtered custom errors (internalClientErrors) and
     * an internal error message
     * to the client
     */
    @Bean
    public GraphQLErrorHandler errorHandler() {
        return new DefaultGraphQLErrorHandler() {
            @Override
            public List<GraphQLError> processErrors(List<GraphQLError> errors) {
                List<GraphQLError> clientErrors = this.filterGraphQLErrors(errors);
                List<GraphQLError> internalClientErrors = errors.stream()
                        .filter(this::isInternalClientError)
                        .map(GraphQLErrorAdapter::new)
                        .collect(Collectors.toList());

                if (clientErrors.size() + internalClientErrors.size() < errors.size()) {
                    clientErrors.add(new GenericGraphQLError("Internal Server Error(s) while executing query"));

                    errors.stream()
                            .filter((error) -> !this.isClientError(error))
                            .forEach((error) -> {
                                if (error instanceof Throwable) {
                                    log.error("Error executing query!", (Throwable) error);
                                } else {
                                    log.error("Error executing query ({}): {}", error.getClass().getSimpleName(), error.getMessage());
                                }
                            });
                }

                List<GraphQLError> finalErrors = new ArrayList<>();
                finalErrors.addAll(clientErrors);
                finalErrors.addAll(internalClientErrors);
                return finalErrors;
            }

            @Override
            protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors) {
                return errors.stream().filter(this::isClientError).collect(Collectors.toList());
            }

            @Override
            protected boolean isClientError(GraphQLError error) {
                return !(error instanceof ExceptionWhileDataFetching || error instanceof Throwable);
            }

            protected boolean isInternalClientError(GraphQLError error) {
                return (error instanceof ExceptionWhileDataFetching) &&
                        (((ExceptionWhileDataFetching) error).getException() instanceof CustomGraphQLException);
            }
        };
    }
}
