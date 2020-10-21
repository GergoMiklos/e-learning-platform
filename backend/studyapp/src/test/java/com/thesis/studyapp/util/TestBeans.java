package com.thesis.studyapp.util;

import com.thesis.studyapp.dto.LoginInputDto;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.model.UserAuthData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestBeans {

    public static final Long USER_ID = 0L;
    public static final String USER_FULL_NAME = "FULL_NAME";
    public static final String USER_CODE = "USER_CODE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PASSWORD = "PASSWORD";

    public static final User USER = User.builder()
            .id(USER_ID)
            .name(USER_FULL_NAME)
            .code(USER_CODE)
            .build();

    public static final LoginInputDto LOGIN_INPUT_DTO = LoginInputDto.builder()
            .username(USER_NAME)
            .password(USER_PASSWORD)
            .build();


    @Autowired
    public UserAuthData userAuthData(PasswordEncoder passwordEncoder) {
        return UserAuthData.builder()
                .username(USER_NAME)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .build();
    }

    public static final Long GROUP_ID = 1L;
    public static final String GROUP_NAME = "GROUP_NAME";
    public static final String GROUP_CODE = "GROUP_CODE";
    public static final String GROUP_DESCRIPTION = "GROUP_DESCRIPTION";
    public static final String GROUP_NEWS = "GROUP_NEWS";
    public static final ZonedDateTime GROUP_NEWS_CHANGED_TIME =
            ZonedDateTime.of(2000, 1, 1, 1, 1, 1, 0, ZoneId.of("UTC"));

    public static final Group GROUP = Group.builder()
            .id(GROUP_ID)
            .name(GROUP_NAME)
            .code(GROUP_CODE)
            .description(GROUP_DESCRIPTION)
            .news(GROUP_NEWS)
            .newsChangedDate(GROUP_NEWS_CHANGED_TIME)
            .build();

}
