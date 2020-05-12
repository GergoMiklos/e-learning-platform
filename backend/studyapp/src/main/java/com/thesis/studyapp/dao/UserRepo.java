package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.UserDTO;
import com.thesis.studyapp.model.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends Neo4jRepository<User, Long> {

    //TODO LTS ID NEM KELL
    @Query("MATCH (u:User) WHERE id(u) IN $0" +
            " WITH u," +
            " [(u)-[:GROUPUSER]-(gu:Group) | id(gu)] AS groupIds," +
            " [(u)-[:GROUPADMIN]-(ga:Group) | id(ga)] AS managedGroupIds," +
            " [(u)--(lts:LiveTestState) | id(lts)] AS liveTestStateIds," +
            " [(u)--(t:Task) | id(t) ] AS createdTaskIds," +
            " [(u)--(t:Test) | id(t) ] AS createdTestIds" +
            " RETURN id(u) AS id, u.name AS name, u.code AS code, groupIds, managedGroupIds," +
            " createdTaskIds, createdTestIds, liveTestStateIds")
    List<UserDTO> getByManyIds(List<Long> ids);

    @Query("MATCH (u:User) WHERE id(u) = $0" +
            " WITH u," +
            " [(u)-[:GROUPUSER]-(gu:Group) | id(gu)] AS groupIds," +
            " [(u)-[:GROUPADMIN]-(ga:Group) | id(ga)] AS managedGroupIds," +
            " [(u)--(lts:LiveTestState) | id(lts)] AS liveTestStateIds," +
            " [(u)--(t:Task) | id(t) ] AS createdTaskIds," +
            " [(u)--(t:Test) | id(t) ] AS createdTestIds" +
            " RETURN id(u) AS id, u.name AS name, u.code AS code, groupIds, managedGroupIds," +
            " createdTaskIds, createdTestIds, liveTestStateIds")
    Optional<UserDTO> getById(Long id);

    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group)" +
            " WHERE id(g) = $0" +
            " WITH u," +
            " [(u)-[:GROUPUSER]-(gu:Group) | id(gu)] AS groupIds," +
            " [(u)-[:GROUPADMIN]-(ga:Group) | id(ga)] AS managedGroupIds," +
            " [(u)--(lts:LiveTestState) | id(lts)] AS liveTestStateIds," +
            " [(u)--(t:Task) | id(t) ] AS createdTaskIds," +
            " [(u)--(t:Test) | id(t) ] AS createdTestIds" +
            " RETURN id(u) AS id, u.name AS name, u.code AS code, groupIds, managedGroupIds," +
            " createdTaskIds, createdTestIds, liveTestStateIds")
    List<UserDTO>  getByGroupId(Long groupId);

}
