package com.thesis.studyapp.service;

import com.thesis.studyapp.dao.GroupRepo;
import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.model.Group;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    @Autowired
    private GroupRepo groupRepo;

    public List<GroupDTO> getGroupsByUserId(Long userId) {
        System.out.println("GroupService: getGroupsByUserId");
        return groupRepo.findByUserId(userId);
    }

    public List<GroupDTO> getGroupsByAdminId(Long userId) {
        System.out.println("GroupService: getGroupsByAdminId");
        return convertToDTO(groupRepo.findByAdminId(userId));
    }

    public GroupDTO getGroupById(Long id) {
        System.out.println("GroupService: getGroupById");
        Optional<Group> group = groupRepo.findById(id);
        return group.map(this::convertToDTO).orElse(null);
    }

    public GroupDTO getGroupDTObyId(Long id) {
        return groupRepo.findByDTOId(id);
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
        //TODO minden convertToEntity
        //groupRepobol lekérdezés, hiányzó adatok kiegészítése!
        //Ez így tulajdonképpen egy update, csak még nem mentünk rá!!!
        return modelMapper.map(group, Group.class);
    }

    private List<Group> convertToEntity(List<GroupDTO> group) {
        //TODO
        //Ezt inkább foreach-csel!
        Type listType = new TypeToken<List<Group>>() {}.getType();
        return modelMapper.map(group, listType);
    }
}
