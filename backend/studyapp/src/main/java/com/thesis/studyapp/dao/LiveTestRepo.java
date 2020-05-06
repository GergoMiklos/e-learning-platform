package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.LiveTestDTO;
import com.thesis.studyapp.model.LiveTest;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveTestRepo extends Neo4jRepository<LiveTest, Long> {

//    @Query("MATCH (g:Group)-[:GROUPLIVETEST]-(lt:LiveTest)" +
//            " WHERE id(g)=$0" +
//            " RETURN lt")
//    List<LiveTest> findByGroupId(Long groupid);
//
//    @Query("MATCH (u:User)-[:GROUPUSER]-(:Group)-[:GROUPLIVETEST]-(lt:LiveTest)" +
//            " WHERE id(u)=$0" +
//            " RETURN lt")
//    List<LiveTest> findByUserId(Long userid);

    //TODO hiba!!! TestID-t List-ként akarja, nem tudja castolni
    @Query("MATCH (lt:LiveTest) WHERE id(lt) IN $0" +
            " WITH lt," +
            " [(lt)--(t:Test) | id(t)] AS testId," +
            " [(lt)-[lts:LIVETESTSTATE]-() | id(lts)] AS liveTestUserStateIds" +
            " RETURN id(lt) AS id, lt.name AS name, liveTestUserStateIds")
    List<LiveTestDTO> getByManyIds(List<Long> ids);
}
