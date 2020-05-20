package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dao.TaskRepo;
import com.thesis.studyapp.dao.UserRepo;
import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class TaskQuery implements GraphQLQueryResolver, GraphQLMutationResolver {
    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    public List<TaskDTO> getByManyIds(List<Long> ids) {
        return taskRepo.getByManyIds(ids);
    }

    public List<TaskDTO> tasks() { return taskRepo.getAll();}

    public Optional<TaskDTO> createTask(Long userId, String question, List<String> answers) {
        User user = userRepo.findById(userId, 0).orElseThrow(()-> new CustomGraphQLException("No user with id: " + userId));
        Task task = new Task();
        task.setQuestion(question);
        task.setAnswers(answers);
        task.setSolution(0);
        task.setOwner(user);
        task = taskRepo.save(task);
        return taskRepo.getById(task.getId());
    }
}
