package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.Test;
import org.springframework.data.repository.CrudRepository;

public interface TestRepo extends CrudRepository<Test, Long> {

    Test findByName(String name);
}
