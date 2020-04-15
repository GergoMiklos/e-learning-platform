package com.thesis.studyapp.web.controller;

import com.thesis.studyapp.dao.TaskRepo;
import com.thesis.studyapp.dao.TestRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.Test;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.web.dto.GroupDTO;
import com.thesis.studyapp.web.dto.TestDTO;
import com.thesis.studyapp.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TestController {

    @Autowired
    TestRepo testRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("tests/id/{id}")
    public TestDTO getTestById(@PathVariable Long id) {
        Optional<Test> test = testRepo.findById(id);
        //if(test.isPresent())
        return modelMapper.map(test.get(), TestDTO.class);
    }

    //Ilyenkor mit kéne csinálni, ha nem tudjuk hánnyal térünk vissza, de mi csak 1et szeretnénk???
    @GetMapping("tests/livetestid/{livetestid}")
    public TestDTO getTestByLiveTestId(@PathVariable Long livetestid) {
        return modelMapper.map(testRepo.getTestByLiveTestId(livetestid).get(), TestDTO.class);
    }

    //POST és PUT is:
//    @GetMapping("tests/ownerid/{ownerid}")
//    public List<Test> getTestByOwnerId(@PathVariable Long ownerid) {
//        return testRepo.getTestByOwnerId(ownerid);
//    }



}