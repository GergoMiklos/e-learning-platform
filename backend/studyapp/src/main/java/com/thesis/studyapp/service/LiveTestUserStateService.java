package com.thesis.studyapp.service;

import com.thesis.studyapp.dao.LiveTestUserStateRepo;
import com.thesis.studyapp.dto.LiveTestUserStateDTO;
import com.thesis.studyapp.model.LiveTestUserState;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class LiveTestUserStateService {

    @Autowired
    LiveTestUserStateRepo liveTestUseStateRepo;

    public LiveTestUserStateDTO getLiveTestUserStateById(Long id) {
        Optional<LiveTestUserState> ltus = liveTestUseStateRepo.findById(id);
        return ltus.map(this::convertToDTO).orElse(null);
    }

    public List<LiveTestUserStateDTO> getLiveTestUserStatesByLiveTest(Long liveTestId) {
        return convertToDTO(liveTestUseStateRepo.findByLiveTestId(liveTestId));
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
