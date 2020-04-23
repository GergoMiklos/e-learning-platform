package com.thesis.studyapp.web.restcontroller;

import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("users/id/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("users/username/{username}")
    public UserDTO getUserByUserName(@PathVariable String username) {
        return userService.getUserByUserName(username);
    }

    @GetMapping("users/batchtest")
    public List<UserDTO> getUserByGroupIds() {
        List<Long> l = new ArrayList<>();
        l.add(new Long(15)); l.add(new Long(16));
        return userService.getUserByGroupIds(l);
    }

}
