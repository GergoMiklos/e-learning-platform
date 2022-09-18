package com.thesis.studyapp.security.service;

import com.thesis.studyapp.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class DefaultUserDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private static final String ROLE_USER = "ROLE_USER";

    private String username;
    private String password;
    private Long userId;

    public static DefaultUserDetails build(User user) {
        if (user.getAuthData() == null) {
            throw new IllegalStateException("UserAuthData needed for converting DefaultUserDetails");
        }

        return DefaultUserDetails.builder()
                .username(user.getAuthData().getUsername())
                .password(user.getAuthData().getPassword())
                .userId(user.getId())
                .build();
    }

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_USER));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
