package com.thesis.studyapp.web.controller;

import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.GroupUserState;
import com.thesis.studyapp.web.dto.GroupDTO;
import com.thesis.studyapp.web.dto.GroupUserStateDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@RestController
public class GroupController {
    @Autowired
    private GroupRepo groupRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/groups/userid/{userid}")
    public List<GroupUserState> getGroupsByUserId(@PathVariable Long userid) {
        List<Group> groups = groupRepo.findByUserId(userid);
        Type listType = new TypeToken<List<GroupDTO>>() {}.getType();
        return modelMapper.map(groups, listType);
    }

    @GetMapping("/groups/adminid/{userid}")
    public List<GroupUserState> getGroupsByAdminId(@PathVariable Long userid) {
        List<Group> groups = groupRepo.findByAdminId(userid);
        Type listType = new TypeToken<List<GroupDTO>>() {}.getType();
        return modelMapper.map(groups, listType);
    }

    @GetMapping("/groups/id/{id}")
    public GroupDTO getGroupsByUserName(@PathVariable Long id) {
        return modelMapper.map(groupRepo.findById(id, 1).get(), GroupDTO.class);
    }


}
