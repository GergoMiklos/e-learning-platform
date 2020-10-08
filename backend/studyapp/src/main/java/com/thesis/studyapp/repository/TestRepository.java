package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.Test;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends Neo4jRepository<Test, Long> {

    List<Test> findByGroupIdOrderByName(Long groupId, @Depth int depth);

    List<Test> findByIdIn(List<Long> ids, @Depth int depth);

}
