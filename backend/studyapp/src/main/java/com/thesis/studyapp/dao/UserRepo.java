package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByUserName(String userName);

}
