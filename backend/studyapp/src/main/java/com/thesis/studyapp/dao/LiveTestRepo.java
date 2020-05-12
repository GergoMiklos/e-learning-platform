package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.LiveTestDTO;
import com.thesis.studyapp.model.LiveTest;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveTestRepo extends Neo4jRepository<LiveTest, Long> {

//TODO MEGOLDVA: TestID-t List-ként akarja, nem tudja castolni M
@Query("MATCH (lt:LiveTest)--(g:Group) WHERE id(g) = $0" +
        " WITH lt," +
        " head([(lt)--(t:Test) | id(t)]) AS testId," +
        " [(lt)-[:TESTSTATE]-(lts:LiveTestState) | id(lts)] AS liveTestStateIds" +
        " RETURN id(lt) AS id, testId, liveTestStateIds")
List<LiveTestDTO> getByGroupId(Long groupId);

    @Query("MATCH (lt:LiveTest) WHERE id(lt) IN $0" +
            " WITH lt," +
            " head([(lt)--(t:Test) | id(t)]) AS testId," +
            " [(lt)-[:TESTSTATE]-(lts:LiveTestState) | id(lts)] AS liveTestStateIds" +
            " RETURN id(lt) AS id, testId, liveTestStateIds")
    List<LiveTestDTO> getByManyIds(List<Long> ids);
}
