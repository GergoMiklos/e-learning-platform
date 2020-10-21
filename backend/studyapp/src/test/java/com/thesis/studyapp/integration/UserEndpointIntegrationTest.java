package com.thesis.studyapp.integration;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.thesis.studyapp.util.TestBeans;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserEndpointIntegrationTest {
    public static final String USER_FULL_NAME = TestBeans.USER_FULL_NAME;
    public static final String USER_CODE = TestBeans.USER_CODE;
    public static final Long USER_ID = TestBeans.USER_ID;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    void queryUser_getsTheUser() throws IOException {
        GraphQLResponse response = graphQLTestTemplate
                .perform("graphql/get-user-query.graphql", createUserIdInput(USER_ID));

        System.out.println(response.getRawResponse().getBody());

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.user.id")).isEqualTo(USER_ID.toString());
        assertThat(response.get("$.data.user.name")).isEqualTo(USER_FULL_NAME);
        assertThat(response.get("$.data.user.code")).isEqualTo(USER_CODE);
    }

    @Test
    void queryUser_error_whenNoUser() throws IOException {
        GraphQLResponse response = graphQLTestTemplate
                .perform("graphql/get-user-query.graphql", createUserIdInput(999L));

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data")).isNull();
        assertThat(response.get("$.errors[0].extensions.NOT_FOUND")).isNotNull();
    }

    public ObjectNode createUserIdInput(Long userId) {
        return JsonNodeFactory.instance.objectNode()
                .put("userId", userId);
    }


    @TestConfiguration
    static class TestHarnessConfig {
        @Bean
        public Neo4j initDatabase() {
            return Neo4jBuilders.newInProcessBuilder()
                    .withDisabledServer()
                    .withFixture(""
                            + String.format("CREATE (user:User {name:'%s', code: '%s'})\n", USER_FULL_NAME, USER_CODE) // id=0
                    )
                    .build();
        }
    }
}
