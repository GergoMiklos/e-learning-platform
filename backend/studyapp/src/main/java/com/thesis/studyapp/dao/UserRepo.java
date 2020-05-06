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

    Optional<User> findByUserName(String userName);

    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group) WHERE id(g)=$0 RETURN u")
    List<User> findByGroupId(Long groupId);

    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group) WHERE id(g) IN $0 RETURN u")
    List<User> findByGroupIds(List<Long> groupIds);

    @Query("MATCH (u:User) WHERE id(u) IN $0 RETURN u")
    List<User> findByIds(List<Long> ids);

    @Query("MATCH (u:User) WHERE id(u) IN $0" +
            " WITH u," +
            " [(u)-[:GROUPUSER]-(gu:Group) | id(gu)] AS groupIds," +
            " [(u)-[:GROUPADMIN]-(ga:Group) | id(ga)] AS managedGroupIds," +
            " [(u)-[lts:LIVETESTSTATE]-() | id(lts)] AS liveTestUserStateIds," +
            " [(u)-[:LIVETESTSTATE]-(lt:LiveTest) | id(lt)] AS liveTestIds," +
            " [(u)--(t:Task) | id(t) ] AS createdTaskIds," +
            " [(u)--(t:Test) | id(t) ] AS createdTestIds" +
            " RETURN id(u) AS id, u.userName AS userName, groupIds, managedGroupIds," +
            " liveTestIds, createdTaskIds, createdTestIds, liveTestUserStateIds")
    List<UserDTO> findByManyIds(List<Long> ids);

    @Query("MATCH (u:User) WHERE id(u) = $0" +
            " WITH u," +
            " [(u)-[:GROUPUSER]-(gu:Group) | id(gu)] AS groupIds," +
            " [(u)-[:GROUPADMIN]-(ga:Group) | id(ga)] AS managedGroupIds," +
            " [(u)-[lts:LIVETESTSTATE]-() | id(lts)] AS liveTestUserStateIds," +
            " [(u)-[:LIVETESTSTATE]-(lt:LiveTest) | id(lt)] AS liveTestIds," +
            " [(u)--(t:Task) | id(t) ] AS createdTaskIds," +
            " [(u)--(t:Test) | id(t) ] AS createdTestIds" +
            " RETURN id(u) AS id, u.userName AS userName, groupIds, managedGroupIds," +
            " liveTestIds, createdTaskIds, createdTestIds, liveTestUserStateIds")
    UserDTO findByUserId(Long id);

}
