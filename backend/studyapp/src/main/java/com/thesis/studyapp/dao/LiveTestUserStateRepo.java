package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.LiveTestUserState;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveTestUserStateRepo extends Neo4jRepository<LiveTestUserState, Long> {

}
