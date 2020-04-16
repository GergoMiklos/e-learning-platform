package com.thesis.studyapp.web.restcontroller;

import com.thesis.studyapp.dto.LiveTestDTO;
import com.thesis.studyapp.service.LiveTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LiveTestContoller {

    @Autowired
    LiveTestService liveTestService;

    @GetMapping("livetests/id/{id}")
    public LiveTestDTO getLiveTestById(@PathVariable Long id) {
        return liveTestService.getLiveTestById(id);
    }

    @GetMapping("livetests/groupid/{groupid}")
    public List<LiveTestDTO> getLiveTestByGroupId(@PathVariable Long groupid) {
        return liveTestService.getLiveTestByGroupId(groupid);
    }

    @GetMapping("livetests/userid/{userid}")
    public List<LiveTestDTO> getLiveTestByUserId(@PathVariable Long userid) {
        return liveTestService.getLiveTestByUserId(userid);
    }
}
