package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.LiveTestStateDTO;
import com.thesis.studyapp.dto.TaskDTO;
import com.thesis.studyapp.dto.TestTaskDTO;
import com.thesis.studyapp.model.Task;
import com.thesis.studyapp.model.TestTask;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestTaskRepo extends Neo4jRepository<TestTask, Long> {

    @Query("MATCH (:Test)--(tt:TestTask)" +
            " WHERE id(tt) IN $0" +
            " WITH tt," +
            " head([(tt:TestTask)--(ta:Task) | ta ]) AS task," +
            " head([(te:Test)--(tt:TestTask) | id(te)]) AS testId" +
            " RETURN DISTINCT id(tt) AS id, tt.level AS level, task.question AS question, task.answers AS answers")
    List<TestTaskDTO> getByManyIds(List<Long> ids);

    @Query("MATCH (:Test)--(tt:TestTask)" +
            " WHERE id(tt) = $0" +
            " WITH tt," +
            " head([(tt:TestTask)--(ta:Task) | ta ]) AS task," +
            " head([(te:Test)--(tt:TestTask) | id(te)]) AS testId" +
            " RETURN DISTINCT id(tt) AS id, tt.level AS level, task.question AS question, task.answers AS answers")
    Optional<TestTaskDTO> getById(Long id);

    //TODO NEM MŰKÖDIK SEMMILYEN FIND!!!!!!!!!
//    @Query("MATCH (:Test)-[tt:TESTTASK]-(:Task)" +
//            " WHERE id(tt) = $0" +
//            " RETURN (:Test)-[tt:TESTTASK]-(:Task)")
//    Optional<TestTask> findById2(Long id);

    //Todo itt majd nem kell külön függvény?
//    @Query("MATCH (tt:TestTask)" +
//            " WHERE id(tt) = $0" +
//            " DELETE tt")
//    void deleteById(Long id);
}
