package com.thesis.studyapp.service;

import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.GroupUserState;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepo groupRepo;

    public List<GroupDTO> getGroupsByUserId(Long userId) {
        return convertToDTO(groupRepo.findByUserId(userId));
    }

    public List<GroupDTO> getGroupsByAdminId(Long userId) { ;
        return convertToDTO(groupRepo.findByAdminId(userId));
    }

    public GroupDTO getGroupById(Long id) {
        return convertToDTO(groupRepo.findById(id).get());
    }


    @Autowired
    private ModelMapper modelMapper;

    private GroupDTO convertToDTO(Group group) {
        return modelMapper.map(group, GroupDTO.class);
    }

    private List<GroupDTO> convertToDTO(List<Group> groups) {
        Type listType = new TypeToken<List<GroupDTO>>() {}.getType();
        return modelMapper.map(groups, listType);
    }

    private Group convertToEntity(GroupDTO group) {
        //TODO
        //groupRepobol lekérdezés, hiányzó adatok kiegészítése!
        return modelMapper.map(group, Group.class);
    }

    private List<Group> convertToEntity(List<GroupDTO> group) {
        //TODO
        //groupRepobol lekérdezés, hiányzó adatok kiegészítése!
        Type listType = new TypeToken<List<Group>>() {}.getType();
        return modelMapper.map(group, listType);
    }
}
