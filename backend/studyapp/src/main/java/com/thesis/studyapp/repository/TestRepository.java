package com.thesis.studyapp.repository;

import com.thesis.studyapp.dto.TestDto;
import com.thesis.studyapp.model.Test;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends Neo4jRepository<Test, Long> {
    String RETURN_TESTDTO = "RETURN id(t) AS id, t.name AS name, t.description AS description";

    @Query("MATCH (t:Test)-[:GROUPTEST]-(g:Group) WHERE id(g) = $0 " + RETURN_TESTDTO + " ORDER BY t.name")
    List<TestDto> getByGroupId(Long groupId);

    @Query("MATCH (t:Test) WHERE id(t) IN $0 " + RETURN_TESTDTO)
    List<TestDto> getByIds(List<Long> ids);

    @Query("MATCH (t:Test) WHERE id(t) = $0 " + RETURN_TESTDTO)
    Optional<TestDto> getById(Long id);
}
