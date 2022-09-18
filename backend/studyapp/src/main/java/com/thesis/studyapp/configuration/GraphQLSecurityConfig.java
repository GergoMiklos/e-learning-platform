package com.thesis.studyapp.configuration;

import graphql.analysis.FieldComplexityCalculator;
import graphql.analysis.FieldComplexityEnvironment;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class GraphQLSecurityConfig {
    public static final int QUERY_MAX_DEPTH = 15;
    public static final int QUERY_MAX_COMPLEXITY = 50;

    @Bean
    @Primary
    @Autowired
    public Instrumentation instrumentation(DataLoaderDispatcherInstrumentation dataLoaderDispatcherInstrumentation) {
        List<Instrumentation> instrumentations = new ArrayList<>();
        instrumentations.add(maxQueryDepth());
        instrumentations.add(maxQueryComplexity());
        instrumentations.add(dataLoaderDispatcherInstrumentation);
        return new ChainedInstrumentation(instrumentations);
    }

    public MaxQueryDepthInstrumentation maxQueryDepth() {
        return new MaxQueryDepthInstrumentation(QUERY_MAX_DEPTH);
    }

    public MaxQueryComplexityInstrumentation maxQueryComplexity() {
        return new MaxQueryComplexityInstrumentation(QUERY_MAX_COMPLEXITY, new DefaultFieldComplexityCalculator());
    }

    private static class DefaultFieldComplexityCalculator implements FieldComplexityCalculator {

        @Override public int calculate(FieldComplexityEnvironment fieldComplexityEnvironment, int i) {
            String fieldName = fieldComplexityEnvironment.getField().getName();
            switch (fieldName) {
                case "studentGroups":
                case "teacherGroups":
                case "students":
                case "teachers":
                case "tests":
                case "studentStatuses":
                case "studentTaskStatuses":
                case "testTasks":
                    return 5;
                default:
                    return 1;
            }
        }
    }

}
