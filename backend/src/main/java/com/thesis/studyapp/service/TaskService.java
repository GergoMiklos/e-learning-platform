package com.thesis.studyapp.service;

import com.thesis.studyapp.dto.TaskInputDto;
import com.thesis.studyapp.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;

public interface TaskService {
    Task getTask(Long taskId);

    Page<Task> searchTasks(Long testId, String searchText, int page);

    Task createTask(TaskInputDto taskInputDto);

    Task setTaskUsage(Long taskId, boolean oneMore);
}
