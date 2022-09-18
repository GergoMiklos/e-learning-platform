package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.StudentTaskStatus;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTaskStatusRepository extends Neo4jRepository<StudentTaskStatus, Long>, ObjectLoader<StudentTaskStatus> {

    List<StudentTaskStatus> findByIdIn(List<Long> ids, @Depth int depth);

}