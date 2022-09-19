package com.thesis.studyapp.integration;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.thesis.studyapp.util.TestBeans;
import org.junit.jupiter.api.Disabled;
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
public class GroupEndpointIntegrationTest {
    public static final String USER_FULL_NAME = TestBeans.USER_FULL_NAME;
    public static final String USER_CODE = TestBeans.USER_CODE;
    public static final Long USER_ID = TestBeans.USER_ID;
    public static final String GROUP_CODE = TestBeans.GROUP_CODE;
    public static final String GROUP_NAME = TestBeans.GROUP_NAME;
    public static final Long GROUP_ID = TestBeans.GROUP_ID;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    @Disabled
    void addStudentToGroupFromCode_addsStudentToGroup() throws IOException {

        GraphQLResponse response = graphQLTestTemplate
                .perform("graphql/add-student-to-group-mutation.graphql", createUserIdAndGroupCodeInput(USER_ID, GROUP_CODE));

        System.out.println(response.getRawResponse().getBody());

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.addStudentToGroupFromCode.id")).isEqualTo(GROUP_ID.toString());
        assertThat(response.get("$.data.addStudentToGroupFromCode.name")).isEqualTo(GROUP_CODE);
        assertThat(response.get("$.data.addStudentToGroupFromCode.students")).isNotNull();
        assertThat(response.get("$.data.addStudentToGroupFromCode.students").length()).isEqualTo(1);
        assertThat(response.get("$.data.addStudentToGroupFromCode.students[0].id")).isEqualTo(USER_ID.toString());
        assertThat(response.get("$.data.addStudentToGroupFromCode.students[0].name")).isEqualTo(USER_FULL_NAME);
    }

    @Test
    void queryUser_error_whenUnAuthorized() throws IOException {
        GraphQLResponse response = graphQLTestTemplate
                .perform("graphql/add-student-to-group-mutation.graphql", createUserIdAndGroupCodeInput(USER_ID, GROUP_CODE));

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data.addStudentToGroupFromCode")).isNull();
        assertThat(response.get("$.errors[0].extensions.UNAUTHORIZED")).isNotNull();
    }

    @Test
    @Disabled
    void queryUser_error_whenNoGroup() throws IOException {
        GraphQLResponse response = graphQLTestTemplate
                .perform("graphql/add-student-to-group-mutation.graphql", createUserIdAndGroupCodeInput(USER_ID, "ANOTHER"));
        ;

        assertThat(response.isOk()).isTrue();
        assertThat(response.get("$.data")).isNull();
        assertThat(response.get("$.errors[0].extensions.NOT_FOUND")).isNotNull();
    }

    private ObjectNode createUserIdAndGroupCodeInput(Long userId, String groupCode) {
        return JsonNodeFactory.instance.objectNode()
                .put("userId", userId)
                .put("groupCode", groupCode);
    }

    @TestConfiguration
    static class TestHarnessConfig {
        @Bean
        public Neo4j initDatabase() {
            return Neo4jBuilders.newInProcessBuilder()
                    .withDisabledServer()
                    .withFixture(""
                            + String.format("CREATE (user:User {name:'%s', code: '%s'})\n", USER_FULL_NAME, USER_CODE) // id=0
                            + String.format("CREATE (group:Group {name:'%s', code: '%s'})\n", GROUP_NAME, GROUP_CODE) // id=1
                    )
                    .build();
        }
    }
}

