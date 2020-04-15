package com.thesis.studyapp.web.restcontroller;

import com.thesis.studyapp.dao.LiveTestUserStateRepo;
import com.thesis.studyapp.dto.LiveTestUserStateDTO;
import com.thesis.studyapp.service.LiveTestUserStateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LiveTestUserStateController {
    @Autowired
    LiveTestUserStateService liveTestUserStateService;

    @GetMapping("livetestuserstates/id/{id}")
    public LiveTestUserStateDTO getLiveTestUserStateById(@PathVariable Long id) {
        return liveTestUserStateService.getLiveTestUserStateById(id);
    }


}
