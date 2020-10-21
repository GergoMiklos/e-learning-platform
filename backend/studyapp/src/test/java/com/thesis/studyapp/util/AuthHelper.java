package com.thesis.studyapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import com.thesis.studyapp.dto.LoginInputDto;
import org.opentest4j.TestAbortedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthHelper {
    public static final LoginInputDto LOGIN_INPUT_DTO = TestBeans.LOGIN_INPUT_DTO;

    @Autowired
    GraphQLTestTemplate graphQLTestTemplate;

    public String createBearerToken() throws IOException {
        GraphQLResponse response = graphQLTestTemplate
                .perform("graphql/user-login-mutation.graphql", createLoginInput());

        if (response.isOk() && response.get("$.data.login") != null && response.get("$.data.login.token") != null) {
            return String.format("Bearer %s", response.get("$.data.login.token"));
        } else {
            throw new TestAbortedException("Could not perform bearer token");
        }
    }

    private ObjectNode createLoginInput() {
        return new ObjectMapper().valueToTree(LOGIN_INPUT_DTO);
    }
}
