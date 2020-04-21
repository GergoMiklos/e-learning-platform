package com.thesis.studyapp.service;

import com.thesis.studyapp.dao.TestRepo;
import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.model.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    TestRepo testRepo;

    public TestDTO getTestById(Long id) {
        Optional<Test> test = testRepo.findById(id);
        return test.map(this::convertToDTO).orElse(null);

    }

    //Ilyenkor mit kéne csinálni, ha nem tudjuk hánnyal térünk vissza, de mi csak 1et szeretnénk???
    public TestDTO getTestByLiveTestId(Long liveTestId) {
        return modelMapper.map(testRepo.getTestByLiveTestId(liveTestId).get(), TestDTO.class);
    }


    @Autowired
    private ModelMapper modelMapper;

    private TestDTO convertToDTO(Test test) {
        return modelMapper.map(test, TestDTO.class);
    }

    private List<TestDTO> convertToDTO(List<Test> test) {
        Type listType = new TypeToken<List<TestDTO>>() {}.getType();
        return modelMapper.map(test, listType);
    }
}
