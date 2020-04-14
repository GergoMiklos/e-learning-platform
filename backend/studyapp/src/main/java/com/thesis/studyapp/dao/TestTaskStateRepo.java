package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.LiveTestUserState;
import com.thesis.studyapp.model.TestTaskState;
import org.springframework.data.repository.CrudRepository;

public interface TestTaskStateRepo extends CrudRepository<TestTaskState, Long> {

}
