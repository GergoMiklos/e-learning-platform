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
        return convertToDto(getTaskById(taskId, 0));
    }

    public List<TaskDto> getTasksByIds(List<Long> ids) {
        return convertToDto(taskRepository.findByIdIn(ids, 0));
    }

    public List<TaskDto> getTasks() {
        return convertToDto(taskRepository.findAll());
    }

    public TaskDto createTask(Long userId, String question, List<String> answers) {
        Task task = Task.builder()
                .question(question)
                .answers(answers)
                .solution(1) //TODO
                .build();
        return convertToDto(taskRepository.save(task));
    }

    private Task getTaskById(Long taskId, int depth) {
        return taskRepository.findById(taskId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No task with id: " + taskId));
    }

    private TaskDto convertToDto(Task task) {
        return TaskDto.build(task);
    }

    private List<TaskDto> convertToDto(List<Task> tasks) {
        return tasks.stream()
                .map(TaskDto::build)
                .collect(Collectors.toList());
    }
}

