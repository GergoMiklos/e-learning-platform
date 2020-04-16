package com.thesis.studyapp.service;

import com.thesis.studyapp.dao.TaskRepo;
import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.model.Task;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepo taskRepo;

    public TaskDTO getTaskById(Long id) {
        Optional<Task> task = taskRepo.findById(id);
        //if(task.isPresent())
        return convertToDTO(task.get());
    }

    public List<TaskDTO> getTasksByTestId(Long testId) {
        return convertToDTO(taskRepo.getTasksByTestId(testId));
    }


    @Autowired
    private ModelMapper modelMapper;

    private TaskDTO convertToDTO(Task task) {
        return modelMapper.map(task, TaskDTO.class);
    }

    private List<TaskDTO> convertToDTO(List<Task> task) {
        Type listType = new TypeToken<List<TaskDTO>>() {}.getType();
        return modelMapper.map(task, listType);
    }
}
