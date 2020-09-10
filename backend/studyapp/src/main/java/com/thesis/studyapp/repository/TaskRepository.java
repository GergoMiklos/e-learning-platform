package com.thesis.studyapp.repository;

import com.thesis.studyapp.dto.TaskDto;
import com.thesis.studyapp.model.Task;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends Neo4jRepository<Task, Long> {
    String RETURN_TASKDTO = " RETURN id(t) AS id, t.question AS question, t.answers AS answers, t.solution AS solution";

    @Query("MATCH (t:Task) WHERE id(t) IN $0" + RETURN_TASKDTO)
    List<TaskDto> getByManyIds(List<Long> ids);

    @Query("MATCH (t:Task) WHERE id(t) = $0" + RETURN_TASKDTO)
    Optional<TaskDto> getById(Long id);

    @Query("MATCH (t:Task)" + RETURN_TASKDTO + "ORDER BY t.usage LIMIT 25")
    List<TaskDto> getAll();
}
