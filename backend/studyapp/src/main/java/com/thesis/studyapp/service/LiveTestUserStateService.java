package com.thesis.studyapp.service;

import com.thesis.studyapp.dao.LiveTestUserStateRepo;
import com.thesis.studyapp.dto.LiveTestUserStateDTO;
import com.thesis.studyapp.model.LiveTestUserState;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class LiveTestUserStateService {

    @Autowired
    LiveTestUserStateRepo liveTestUseStateRepo;

    public LiveTestUserStateDTO getLiveTestUserStateById(Long id) {
        return convertToDTO(liveTestUseStateRepo.findById(id).get());
    }


    @Autowired
    private ModelMapper modelMapper;

    private LiveTestUserStateDTO convertToDTO(LiveTestUserState liveTestUserState) {
        return modelMapper.map(liveTestUserState, LiveTestUserStateDTO.class);
    }

    private List<LiveTestUserStateDTO> convertToDTO(List<LiveTestUserState> liveTestUserState) {
        Type listType = new TypeToken<List<LiveTestUserStateDTO>>() {}.getType();
        return modelMapper.map(liveTestUserState, listType);
    }
}
