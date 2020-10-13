package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends Neo4jRepository<Task, Long>, ObjectLoader<Task> {

    List<Task> findByIdIn(List<Long> ids, @Depth int depth);

    Page<Task> findByQuestionContainingIgnoreCaseOrderByUsageDesc(String searchString, Pageable pageable, @Depth int depth);

    Page<Task> findAllByOrderByUsageDesc(Pageable pageable, @Depth int depth);

    @Query("MATCH (test:Test)-->(:TestTask)-->(existing:Task)<--(:TestTask)<--(:Test)-->(:TestTask)-->(similar:Task)" +
            " WHERE id(test)=36 AND id(existing)<>id(similar)" +
            " RETURN (similar:Task)-->(:TaskAnswer)")
        //" ORDER BY similar.usage DESC LIMIT 25"
    Page<Task> findBySimilarTasksForTest(Pageable pageable, @Param("testId") Long testId);

}
