package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.LoginInputDto;
import com.thesis.studyapp.dto.RegisterInputDto;
import com.thesis.studyapp.dto.TokenDto;
import com.thesis.studyapp.exception.BadCredentialsException;
import com.thesis.studyapp.exception.InvalidInputException;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserAuthData;
import com.thesis.studyapp.repository.UserRepository;
import com.thesis.studyapp.security.jwt.JwtUtil;
import com.thesis.studyapp.security.service.DefaultUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public void register(RegisterInputDto registerInputDto) {
        registerInputDto.validate();

        if (!isUsernameAlreadyRegistered(registerInputDto.getUsername())) {
            userRepository.save(convertToUser(registerInputDto));
        } else {
            throw new InvalidInputException("Username is already registered", "username");
        }
    }

    @Transactional
    public TokenDto login(LoginInputDto loginInputDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginInputDto.getUsername(), loginInputDto.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwtToken = jwtUtil.generateJwtToken(authentication);
            DefaultUserDetails userDetails = (DefaultUserDetails) authentication.getPrincipal();

            return TokenDto.builder()
                    .token(jwtToken)
                    .userId(userDetails.getUserId())
                    .type("Bearer")
                    .build();

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }
    }

    public boolean isUsernameAlreadyRegistered(String username) {
        return userRepository.existsByAuthDataUsernameIgnoreCase(username);
    }

    private User convertToUser(RegisterInputDto registerInputDto) {
        return User.builder()
                .name(registerInputDto.getName())
                .code(createUserCode())
                .authData(UserAuthData.builder()
                        .password(passwordEncoder.encode(registerInputDto.getPassword()))
                        .username(registerInputDto.getUsername())
                        .build())
                .build();
    }

    private String createUserCode() {
        String code;
        do {
            code = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        } while (userRepository.existsByCodeIgnoreCase(code));
        return code;
    }

}