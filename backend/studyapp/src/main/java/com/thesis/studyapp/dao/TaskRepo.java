package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.TaskDTO;
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
    List<Task> getByTestId(Long testId);

    @Query("MATCH (t:Task)" +
            " WHERE id(t) IN $0" +
            " RETURN id(t) AS id, t.question AS question, t.answers AS answers, t.solution AS solution, t.level AS level")
    List<TaskDTO> getByManyIds(List<Long> ids);

    @Query("MATCH (t:Task)" +
            " RETURN id(t) AS id, t.question AS question, t.answers AS answers, t.solution AS solution, t.level AS level" +
            " LIMIT 25")
    List<TaskDTO> getAll();
}
