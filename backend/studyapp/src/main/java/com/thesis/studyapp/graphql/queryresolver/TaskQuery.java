package com.thesis.studyapp.graphql.queryresolver;

import com.thesis.studyapp.dao.TaskRepo;
import com.thesis.studyapp.dto.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskQuery {
    @Autowired
    TaskRepo taskRepo;

    public List<TaskDTO> getByManyIds(List<Long> ids) {
        return taskRepo.findByManyIds(ids);
    }
}
