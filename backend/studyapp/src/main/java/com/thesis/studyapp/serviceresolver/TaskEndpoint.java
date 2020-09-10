package com.thesis.studyapp.serviceresolver;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.TaskRepository;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TaskEndpoint implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskDto> getTasksByIds(List<Long> ids) {
        return taskRepository.getByManyIds(ids);
    }

    public List<TaskDto> getTasks() {
        return taskRepository.getAll();
    }

    public Optional<TaskDto> createTask(Long userId, String question, List<String> answers) {
        User user = userRepository.findById(userId, 0)
                .orElseThrow(() -> new CustomGraphQLException("No user with id: " + userId));
        Task task = new Task();
        task.setQuestion(question);
        task.setAnswers(answers);
        task.setSolution(0);
        task.setOwner(user);
        task = taskRepository.save(task);
        return taskRepository.getById(task.getId());
    }
}
