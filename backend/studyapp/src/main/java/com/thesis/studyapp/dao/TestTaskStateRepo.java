package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.TestTaskState;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTaskStateRepo extends Neo4jRepository<TestTaskState, Long> {

}
