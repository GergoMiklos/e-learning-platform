package com.thesis.studyapp.web.restcontroller;

import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.GroupUserState;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.service.GroupService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;


@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/groups/userid/{userid}")
    public List<GroupDTO> getGroupsByUserId(@PathVariable Long userid) {
        return groupService.getGroupsByUserId(userid);
    }

    @GetMapping("/groups/adminid/{userid}")
    public List<GroupDTO> getGroupsByAdminId(@PathVariable Long userid) {
        return groupService.getGroupsByAdminId(userid);
    }

    @GetMapping("/groups/id/{id}")
    public GroupDTO getGroupsByUserName(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }


}
