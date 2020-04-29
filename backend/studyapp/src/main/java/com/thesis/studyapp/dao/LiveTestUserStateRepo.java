package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.LiveTestUserStateDTO;
import com.thesis.studyapp.model.LiveTestUserState;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveTestUserStateRepo extends Neo4jRepository<LiveTestUserState, Long> {

    //Todo mi a faszért van több találat?
    @Query("MATCH ()-[lts:LIVETESTSTATE]-(lt:LiveTest)" +
            " WHERE id(lt) = $0" +
            " RETURN DISTINCT id(lts) AS id")
    List<LiveTestUserStateDTO> findByLiveTestId(Long liveTestId);

    @Query("MATCH ()-[lts:LIVETESTSTATE]-(lt:LiveTest)" +
            " WHERE id(lts) IN $0" +
            " RETURN DISTINCT id(lts) AS id")
    List<LiveTestUserStateDTO> findByManyIds(List<Long> ids);
}
