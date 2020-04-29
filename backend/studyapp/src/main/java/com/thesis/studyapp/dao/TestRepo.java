package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.TestDTO;
import com.thesis.studyapp.model.Test;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepo extends Neo4jRepository<Test, Long> {

    Test findByName(String name);

    @Query("MATCH (lt:LiveTest)-[:LIVETESTTEST]->(t:Test)" +
            " WHERE id(lt)=$0" +
            " RETURN t")
    Optional<Test> getTestByLiveTestId(Long livetestid);

    @Query("MATCH (t:Test) WHERE id(t) IN $0 " +
            "RETURN id(t) AS id, t.name AS name")
    List<TestDTO> getByManyIds(List<Long> ids);
}
