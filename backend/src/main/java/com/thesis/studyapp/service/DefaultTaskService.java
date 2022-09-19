package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TaskInputDto;
import com.thesis.studyapp.exception.NotFoundException;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.TaskAnswer;
import com.thesis.studyapp.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultTaskService implements TaskService {

    private final TaskRepository taskRepository;

    @Override public Task getTask(Long taskId) {
        return getTaskById(taskId, 1);
    }

    @Override public Page<Task> searchTasks(Long testId, @Nullable String searchText, int page) {
        Pageable pageable = PageRequest.of(page, 25, Sort.Direction.DESC, "usage");

        if (searchText != null && !searchText.trim().isEmpty()) {
            return taskRepository.findByQuestionContainingIgnoreCaseOrderByUsageDesc(searchText, pageable, 1);
        } else {
            return taskRepository.findAllByOrderByUsageDesc(pageable, 1);
//            return taskRepository.findBySimilarTasksForTest(testId);
        }
    }

    @Override public Task createTask(TaskInputDto taskInputDto) {
        return taskRepository.save(convertInputToTask(taskInputDto), 1);
    }

    @Override @Transactional
    public Task setTaskUsage(Long taskId, boolean oneMore) {
        Task task = getTaskById(taskId, 1);
        if (oneMore) {
            task.setUsage(task.getUsage() + 1);
        } else {
            task.setUsage(Math.max(task.getUsage() - 1, 0));
        }
        return taskRepository.save(task);
    }

    private Task getTaskById(Long taskId, int depth) {
        return taskRepository.findById(taskId, depth)
                .orElseThrow(() -> new NotFoundException("No task with id: " + taskId));
    }

    private Task convertInputToTask(TaskInputDto taskInputDto) {
        Random random = new Random();
        int solutionNumber = 1;
        Set<TaskAnswer> answers = new HashSet<>();

        TaskAnswer correct = TaskAnswer.builder()
                .answer(taskInputDto.getCorrectAnswer())
                .number(solutionNumber)
                .build();
        answers.add(correct);

        Set<TaskAnswer> wrongs = taskInputDto.getIncorrectAnswers().stream()
                .map(answer -> TaskAnswer.builder()
                        .answer(answer)
                        .number(random.nextInt(1000) + 1)
                        .build()
                )
                .collect(Collectors.toSet());
        answers.addAll(wrongs);

        return Task.builder()
                .question(taskInputDto.getQuestion())
                .answers(answers)
                .solutionNumber(solutionNumber)
                .usage(0)
                .build();
    }

}

