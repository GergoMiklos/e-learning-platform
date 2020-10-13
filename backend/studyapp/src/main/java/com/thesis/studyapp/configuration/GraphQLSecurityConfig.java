package com.thesis.studyapp.configuration;

import graphql.analysis.FieldComplexityCalculator;
import graphql.analysis.FieldComplexityEnvironment;
import graphql.analysis.MaxQueryComplexityInstrumentation;
import graphql.analysis.MaxQueryDepthInstrumentation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLSecurityConfig {
    public static final int QUERY_MAX_DEPTH = 5;
    public static final int QUERY_MAX_COMPLEXITY = 20;

    @Bean
    public MaxQueryDepthInstrumentation maxQueryDepth() {
        return new MaxQueryDepthInstrumentation(QUERY_MAX_DEPTH);
    }

    @Bean
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
                    return 3;
                default:
                    return 1;
            }
        }
    }


}
