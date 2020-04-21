package com.thesis.studyapp;

import com.thesis.studyapp.dto.LiveTestUserStateDTO;
import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.model.LiveTestUserState;
import com.thesis.studyapp.model.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper;
    }

}
