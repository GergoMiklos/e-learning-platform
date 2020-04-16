package com.thesis.studyapp.web.restcontroller;

import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
