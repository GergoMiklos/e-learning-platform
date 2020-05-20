package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.LiveTestStateDTO;
import com.thesis.studyapp.model.LiveTestState;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveTestStateRepo extends Neo4jRepository<LiveTestState, Long> {

    @Query("MATCH (lts:LiveTestState)--(lt:LiveTest)" +
            " WHERE id(lt) = $0" +
            " WITH lts," +
            " head([(lts:LiveTestState)--(us:User) | id(us)]) AS userId," +
            " head([(lts:LiveTestState)--(:LiveTest)--(t:Test) | id(t)]) AS testId," +
            " head([(lts:LiveTestState)--(ct:Task) | id(ct)]) AS currentTaskId" +
            " RETURN DISTINCT id(lts) AS id, userId, testId, currentTaskId")
    List<LiveTestStateDTO> getByLiveTestId(Long liveTestId);

    @Query("MATCH (lts:LiveTestState)" +
            " WHERE id(lts) IN $0" +
            " WITH lts," +
            " head([(lts:LiveTestState)--(us:User) | id(us)]) AS userId," +
            " head([(lts:LiveTestState)--(:LiveTest)--(t:Test) | id(t)]) AS testId," +
            " head([(lts:LiveTestState)--(ct:Task) | id(ct)]) AS currentTaskId" +
            " RETURN DISTINCT id(lts) AS id, userId, testId, currentTaskId")
    List<LiveTestStateDTO> getByManyIds(List<Long> ids);
}
