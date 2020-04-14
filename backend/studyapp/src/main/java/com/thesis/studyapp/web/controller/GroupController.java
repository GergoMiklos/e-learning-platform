package com.thesis.studyapp.web.controller;

import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.model.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GroupController {
    @Autowired
    private GroupRepo groupRepo;

    @GetMapping("/groups/{username}")
    public List<Group> getGroupsByUserName(@PathVariable String username) {
        return groupRepo.findByUserName(username);
    }
}
