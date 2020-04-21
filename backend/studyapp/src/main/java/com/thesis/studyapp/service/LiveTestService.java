package com.thesis.studyapp.service;

import com.thesis.studyapp.dao.LiveTestRepo;
import com.thesis.studyapp.dto.LiveTestDTO;
import com.thesis.studyapp.model.LiveTest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class LiveTestService {

    @Autowired
    LiveTestRepo liveTestRepo;

    public LiveTestDTO getLiveTestById(Long id) {
        Optional<LiveTest> lt = liveTestRepo.findById(id);
        return lt.map(this::convertToDTO).orElse(null);
    }

//    public List<LiveTestDTO> getLiveTestByGroupId(Long groupId) {
//        return convertToDTO(liveTestRepo.findByGroupId(groupId));
//    }
//
//    public List<LiveTestDTO> getLiveTestByUserId(Long userId) {
//        return convertToDTO(liveTestRepo.findByUserId(userId));
//    }


    @Autowired
    private ModelMapper modelMapper;

    private LiveTestDTO convertToDTO(LiveTest liveTest) {
        return modelMapper.map(liveTest, LiveTestDTO.class);
    }

    private List<LiveTestDTO> convertToDTO(List<LiveTest> liveTest) {
        Type listType = new TypeToken<List<LiveTestDTO>>() {}.getType();
        return modelMapper.map(liveTest, listType);
    }
}
