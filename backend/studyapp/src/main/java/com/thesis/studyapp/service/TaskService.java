package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.TaskInput;
import com.thesis.studyapp.dto.TaskSearchResultDto;
import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.TaskAnswer;
import com.thesis.studyapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskDto getTask(Long taskId) {
        return convertToDto(getTaskById(taskId, 1));
    }

    public List<TaskDto> getTasksByIds(List<Long> ids) {
        return convertToDto(taskRepository.findByIdIn(ids, 1));
    }

    public TaskSearchResultDto searchTasks(@Nullable String searchText, int page) {
        Pageable pageable = PageRequest.of(page, 25, Sort.Direction.DESC, "usage");
        if (searchText != null && !searchText.trim().isEmpty()) {
            //List<String> searchWords = searchText.trim().split("\\s+").;
            return convertToDto(taskRepository.findByQuestionContainingIgnoreCase(searchText, pageable, 1));
        } else {
            return convertToDto(taskRepository.findAll(pageable, 1));
        }
    }

    //todo convertInputToTask...
    public TaskDto createTask(TaskInput taskInput) {
        int solutionNumber = taskInput.getIncorrectAnswers().size();
        Stream<TaskAnswer> allAnswer = Stream.concat(
                Stream.of(taskInput.getCorrectAnswer())
                        .map(answer -> TaskAnswer.builder()
                                .answer(answer)
                                .number(solutionNumber)
                                .build()),
                taskInput.getIncorrectAnswers().stream()
                        .map(answer -> TaskAnswer.builder()
                                .answer(answer)
                                .number(taskInput.getIncorrectAnswers().indexOf(answer))
                                .build())
        );
        Task task = Task.builder()
                .question(taskInput.getQuestion())
                .answers(allAnswer.collect(Collectors.toSet()))
                .solutionNumber(solutionNumber)
                .usage(0L)
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

    private TaskSearchResultDto convertToDto(Page<Task> tasks) {
        return TaskSearchResultDto.builder()
                .totalPages(tasks.getTotalPages())
                .totalElements(tasks.getTotalElements())
                .tasks(convertToDto(tasks.getContent()))
                .build();
    }
}

