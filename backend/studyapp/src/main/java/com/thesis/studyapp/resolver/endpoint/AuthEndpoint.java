package com.thesis.studyapp.resolver.endpoint;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.LoginInputDto;
import com.thesis.studyapp.dto.RegisterInputDto;
import com.thesis.studyapp.dto.TokenDto;
import com.thesis.studyapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final AuthService authService;

    public void register(RegisterInputDto input) {
        authService.register(input);
    }

    public TokenDto login(LoginInputDto input) {
        return authService.login(input);
    }

    public boolean isUsernameAlreadyRegistered(String username) {
        return authService.isUsernameAlreadyRegistered(username);
    }

}
