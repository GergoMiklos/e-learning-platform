package com.thesis.studyapp.configuration;

import com.thesis.studyapp.util.DateUtil;
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
