package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskDto getTask(Long taskId) {
        return TaskDto.build(getTaskById(taskId, 0));
    }

    public List<TaskDto> getTasksByIds(List<Long> ids) {
        return taskRepository.findByIdIn(ids, 0).stream()
                .map(TaskDto::build)
                .collect(Collectors.toList());
    }

    public List<TaskDto> getTasks() {
        return taskRepository.findAll().stream()
                .map(TaskDto::build)
                .collect(Collectors.toList());
    }

    public TaskDto createTask(Long userId, String question, List<String> answers) {
        Task task = Task.builder()
                .question(question)
                .answers(answers)
                .solution(1) //TODO
                .build();
        return TaskDto.build(taskRepository.save(task));
    }

    private Task getTaskById(Long taskId, int depth) {
        return taskRepository.findById(taskId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No task with id: " + taskId));
    }
}

