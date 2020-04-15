package com.thesis.studyapp.web.controller;

import com.thesis.studyapp.dao.TaskRepo;
import com.thesis.studyapp.dao.TestRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.web.dto.GroupDTO;
import com.thesis.studyapp.web.dto.TaskDTO;
import com.thesis.studyapp.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("tasks/id/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskRepo.findById(id);
        //if(task.isPresent())
        return modelMapper.map(task.get(), TaskDTO.class);
    }

    @GetMapping("tasks/testid/{id}")
    public List<TaskDTO> getTasksByTestId(@PathVariable Long id) {
        Type listType = new TypeToken<List<TaskDTO>>() {}.getType();
        return modelMapper.map(taskRepo.getTasksByTestId(id), listType);
    }

}
