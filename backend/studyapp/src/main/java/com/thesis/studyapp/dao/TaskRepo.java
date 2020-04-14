package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepo extends CrudRepository<Task, Long> {

}
