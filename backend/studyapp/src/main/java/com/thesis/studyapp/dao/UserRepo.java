package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.User;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {

    @Depth(2)
    User findByUserName(String userName);
}
