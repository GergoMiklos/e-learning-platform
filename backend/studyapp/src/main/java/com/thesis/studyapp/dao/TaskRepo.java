package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.Task;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends Neo4jRepository<Task, Long> {

    @Query("MATCH (te:Test)-[:TESTTASK]->(ta:Task)" +
            " WHERE id(te)=$0" +
            " RETURN ta")
    List<Task> getTasksByTestId(Long id);
}
