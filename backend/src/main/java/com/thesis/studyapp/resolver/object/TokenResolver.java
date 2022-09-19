package com.thesis.studyapp.resolver.object;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.thesis.studyapp.dto.TokenDto;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.util.DataLoaderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class TokenResolver implements GraphQLResolver<TokenDto> {

    private final DataLoaderUtil dataLoaderUtil;

    public CompletableFuture<User> user(TokenDto tokenDto) {
        return dataLoaderUtil.loadData(User.builder().id(tokenDto.getUserId()).build(), DataLoaderUtil.USER_LOADER);
    }

}
