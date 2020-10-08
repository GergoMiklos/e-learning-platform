package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.TestTask;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestTaskRepository extends Neo4jRepository<TestTask, Long> {

    List<TestTask> findByTestIdOrderByLevel(Long testId, @Depth int depth);

    List<TestTask> findByIdIn(List<Long> ids, @Depth int depth);

    List<TestTask> save(List<TestTask> testTasks, @Depth int depth);

    //TODO NEM MŰKÖDIK SEMMILYEN FIND RELatipnship entityként (ezért már nem az)
}
