package com.thesis.studyapp.web.restcontroller;

import com.thesis.studyapp.dao.TaskRepo;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.service.TaskService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("tasks/id/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping("tasks/testid/{id}")
    public List<TaskDTO> getTasksByTestId(@PathVariable Long id) {
        return taskService.getTasksByTestId(id);
    }

}
