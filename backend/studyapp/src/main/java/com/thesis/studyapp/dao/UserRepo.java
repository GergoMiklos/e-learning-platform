package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends Neo4jRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group) WHERE id(g)=$0 RETURN u")
    List<User> findByGroupId(Long groupId);

    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group) WHERE id(g) IN $0 RETURN u")
    List<User> findByGroupIds(List<Long> groupIds);

    @Query("MATCH (u:User) WHERE id(u) IN $0 RETURN u")
    List<User> findByIds(List<Long> ids);

}
