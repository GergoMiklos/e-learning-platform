package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends Neo4jRepository<User, Long> {

    Optional<User> findByUserName(String userName);

}
