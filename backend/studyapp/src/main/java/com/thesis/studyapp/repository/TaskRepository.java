package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends Neo4jRepository<Task, Long> {

    List<Task> findByIdIn(List<Long> ids, @Depth int depth);

    Page<Task> findByQuestionContainingIgnoreCase(String searchString, Pageable pageable, @Depth int depth);

}
