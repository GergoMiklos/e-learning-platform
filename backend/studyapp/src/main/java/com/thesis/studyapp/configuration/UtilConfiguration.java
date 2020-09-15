package com.thesis.studyapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfiguration {

    @Bean
    public DateUtil dateTimeUtil() {
        return new DateUtil();
    }

    //Todo security
}
