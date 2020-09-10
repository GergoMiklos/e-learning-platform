package com.thesis.studyapp.repository;

import com.thesis.studyapp.dto.TestTaskDto;
import com.thesis.studyapp.model.TestTask;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestTaskRepository extends Neo4jRepository<TestTask, Long> {
    String RETURN_TESTTASKDTO =
            " WITH tt," +
                    " head([(tt:TestTask)--(task:Task) | id(task) ]) AS taskId" +
                    " RETURN DISTINCT id(tt) AS id, tt.level AS level, taskId";

    @Query("MATCH (tt:TestTask) WHERE id(tt) IN $0" + RETURN_TESTTASKDTO)
    List<TestTaskDto> getByIds(List<Long> ids);

    @Query("MATCH (tt:TestTask) WHERE id(tt) = $0" + RETURN_TESTTASKDTO)
    Optional<TestTaskDto> getById(Long id);

    @Query("MATCH (t:Test)--(tt:TestTask)WHERE id(t) = $0" + RETURN_TESTTASKDTO + " ORDER BY tt.level, t.question")
    List<TestTaskDto> getByTestId(Long id);

    //TODO NEM MŰKÖDIK SEMMILYEN FIND!! (MERT REL.ENTITY VOLT)

}
