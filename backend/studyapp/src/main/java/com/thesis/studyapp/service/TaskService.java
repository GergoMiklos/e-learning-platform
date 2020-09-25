package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TaskInput;
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

    public Task getTask(Long taskId) {
        return getTaskById(taskId, 1);
    }

    public List<Task> getTasksByIds(List<Long> ids) {
        return taskRepository.findByIdIn(ids, 1);
    }

    public Page<Task> searchTasks(@Nullable String searchText, int page) {
        Pageable pageable = PageRequest.of(page, 25, Sort.Direction.DESC, "usage");
        if (searchText != null && !searchText.trim().isEmpty()) {
            //List<String> searchWords = searchText.trim().split("\\s+").;
            return taskRepository.findByQuestionContainingIgnoreCase(searchText, pageable, 1);
        } else {
            return taskRepository.findAll(pageable, 1);
        }
    }

    //todo convertInputToTask...
    public Task createTask(TaskInput taskInput) {
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
                .usage(0)
                .build();
        return taskRepository.save(task, 1);
    }

    private Task getTaskById(Long taskId, int depth) {
        return taskRepository.findById(taskId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No task with id: " + taskId));
    }

}

