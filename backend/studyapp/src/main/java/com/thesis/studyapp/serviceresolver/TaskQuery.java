package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.TaskRepo;
import com.thesis.studyapp.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskQuery implements GraphQLQueryResolver {
    @Autowired
    TaskRepo taskRepo;

    public List<TaskDTO> getByManyIds(List<Long> ids) {
        return taskRepo.getByManyIds(ids);
    }

    public List<TaskDTO> tasks() { return taskRepo.getAll();}
}
