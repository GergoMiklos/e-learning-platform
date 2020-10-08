package com.thesis.studyapp.security.service;

import com.thesis.studyapp.exception.UnauthorizedException;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {
    UserRepository userRepository;

    @Override public DefaultUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return DefaultUserDetails.build(userRepository.findByAuthDataUsernameIgnoreCase(username, 1)
                .orElseThrow(() -> new UnauthorizedException("User not found")));
    }

}
