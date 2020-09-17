package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.dto.TaskInputDto;
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

import java.util.List;
import java.util.Optional;
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

    public TaskSearchResultDto searchTasks(Optional<String> searchText, int page) {
        Pageable pageable = PageRequest.of(page, 25, Sort.Direction.DESC, "usage");
        if(searchText.isPresent() && !searchText.get().trim().isEmpty()) {
            return convertToDto(taskRepository.findByQuestionContainingIgnoreCase(searchText.get(), pageable,  1));
        } else {
            return convertToDto(taskRepository.findAll(pageable, 1));
        }
    }

    //todo convertInputToTask...
    public TaskDto createTask(TaskInputDto taskInputDto) {
        int solutionNumber = taskInputDto.getIncorrectAnswers().size();
        Stream<TaskAnswer> allAnswer = Stream.concat(
                Stream.of(taskInputDto.getCorrectAnswer())
                        .map(answer -> TaskAnswer.builder()
                                .answer(answer)
                                .number(solutionNumber)
                                .build()),
                taskInputDto.getIncorrectAnswers().stream()
                        .map(answer -> TaskAnswer.builder()
                                .answer(answer)
                                .number(taskInputDto.getIncorrectAnswers().indexOf(answer))
                                .build())
        );
        Task task = Task.builder()
                .question(taskInputDto.getQuestion())
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

