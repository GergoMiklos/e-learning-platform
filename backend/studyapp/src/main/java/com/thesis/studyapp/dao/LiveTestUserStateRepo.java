package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.LiveTestUserState;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LiveTestUserStateRepo extends Neo4jRepository<LiveTestUserState, Long> {

    @Query("MATCH (lt:LiveTest)-[:LIVETESTSTATE]-() WHERE id(lt)=$0 RETURN p")
    List<LiveTestUserState> findByLiveTestId(Long groupId);
}
