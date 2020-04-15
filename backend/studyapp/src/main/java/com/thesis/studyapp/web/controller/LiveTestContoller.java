package com.thesis.studyapp.web.controller;

import com.thesis.studyapp.dao.LiveTestRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.web.dto.LiveTestDTO;
import com.thesis.studyapp.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

@RestController
public class LiveTestContoller {

    @Autowired
    LiveTestRepo liveTestRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("livetests/id/{id}")
    public LiveTestDTO getLiveTestById(@PathVariable Long id) {
        return modelMapper.map(liveTestRepo.findById(id).get(), LiveTestDTO.class);
    }

    @GetMapping("livetests/groupid/{groupid}")
    public List<LiveTestDTO> getLiveTestByGroupId(@PathVariable Long groupid) {
        Type listType = new TypeToken<List<LiveTestDTO>>() {}.getType();
        return modelMapper.map(liveTestRepo.findByGroupId(groupid), listType);
    }

    @GetMapping("livetests/userid/{userid}")
    public List<LiveTestDTO> getLiveTestByUserId(@PathVariable Long userid) {
        Type listType = new TypeToken<List<LiveTestDTO>>() {}.getType();
        return modelMapper.map(liveTestRepo.findByUserId(userid), listType);
    }
}
