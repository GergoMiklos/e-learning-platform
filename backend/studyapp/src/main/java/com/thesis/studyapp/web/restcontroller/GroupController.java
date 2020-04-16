package com.thesis.studyapp.web.restcontroller;

import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
    public GroupDTO getGroupsById(@PathVariable Long id) {
        return groupService.getGroupById(id);
    }


}
