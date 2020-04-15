package com.thesis.studyapp.web.controller;

import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.web.dto.GroupDTO;
import com.thesis.studyapp.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("users/id/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return modelMapper.map(userRepo.findById(id).get(), UserDTO.class);
    }

    @GetMapping("users/username/{username}")
    public UserDTO getUserByUserName(@PathVariable String username) {
        return modelMapper.map(userRepo.findByUserName(username).get(), UserDTO.class);
    }

}
