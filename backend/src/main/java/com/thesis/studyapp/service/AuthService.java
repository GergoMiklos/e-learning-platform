package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.LoginInputDto;
import com.thesis.studyapp.dto.RegisterInputDto;
import com.thesis.studyapp.dto.TokenDto;

public interface AuthService {
    void register(RegisterInputDto registerInputDto);

    TokenDto login(LoginInputDto loginInputDto);

    boolean isUsernameAlreadyRegistered(String username);
}
