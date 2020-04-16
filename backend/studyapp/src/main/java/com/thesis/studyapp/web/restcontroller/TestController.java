package com.thesis.studyapp.web.restcontroller;

import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping("tests/id/{id}")
    public TestDTO getTestById(@PathVariable Long id) {
        return testService.getTestById(id);
    }

    //Ilyenkor mit kéne csinálni, ha nem tudjuk hánnyal térünk vissza, de mi csak 1et szeretnénk???
    @GetMapping("tests/livetestid/{livetestid}")
    public TestDTO getTestByLiveTestId(@PathVariable Long livetestid) {
        return testService.getTestByLiveTestId(livetestid);
    }

    //POST és PUT is:
//    @GetMapping("tests/ownerid/{ownerid}")
//    public List<Test> getTestByOwnerId(@PathVariable Long ownerid) {
//        return testRepo.getTestByOwnerId(ownerid);
//    }



}