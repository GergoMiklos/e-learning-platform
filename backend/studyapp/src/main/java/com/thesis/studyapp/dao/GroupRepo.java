package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.model.Group;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepo extends Neo4jRepository<Group, Long> {

    Optional<Group> findByCode(String name);

//TODO nem kell mindig minden id PL User és Admin
    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group) WHERE id(u) = $0 " +
            "WITH g, " +
            "[(g)-[:GROUPUSER]-(us:User) | id(us)] AS userIds, " +
            "[(g)-[:GROUPADMIN]-(ad:User) | id(ad)] AS adminIds, " +
            "head([(g)--(n:News) | id(n)]) AS newsId, " +
            "[(g)-[:GROUPLIVETEST]-(lt:LiveTest) | id(lt)] AS liveTestId " +
            "RETURN id(g) AS id, g.name AS name, g.code AS code, g.description AS description, " +
            "newsId, userIds, adminIds, liveTestIds")
    List<GroupDTO> getByUserId(Long userId);

    @Query("MATCH (u:User)-[:GROUPADMIN]-(g:Group) WHERE id(u) = $0 " +
            "WITH g, " +
            "[(g)-[:GROUPUSER]-(us:User) | id(us)] AS userIds, " +
            "[(g)-[:GROUPADMIN]-(ad:User) | id(ad)] AS adminIds, " +
            "head([(g)--(n:News) | id(n)]) AS newsId, " +
            "[(g)-[:GROUPLIVETEST]-(lt:LiveTest) | id(lt)] AS liveTestIds " +
            "RETURN id(g) AS id, g.name AS name, g.code AS code, g.description AS description, " +
            "newsId, userIds, adminId, liveTestIds")
    List<GroupDTO> getByAdminId(Long userId);

    @Query("MATCH (g:Group) WHERE id(g) = $0 " +
            "WITH g, " +
            "[(g)-[:GROUPUSER]-(us:User) | id(us)] AS userIds, " +
            "[(g)-[:GROUPADMIN]-(ad:User) | id(ad)] AS adminIds, " +
            "head([(g)--(n:News) | id(n)]) AS newsId, " +
            "[(g)-[:GROUPLIVETEST]-(lt:LiveTest) | id(lt)] AS liveTestIds " +
            "RETURN id(g) AS id, g.name AS name, g.code AS code, g.description AS description, " +
            "newsId, userIds, adminIds, liveTestIds")
    Optional<GroupDTO> getById(Long groupId);

    @Query("MATCH (g:Group) WHERE id(g) IN $0 " +
            "WITH g, " +
            "[(g)-[:GROUPUSER]-(us:User) | id(us)] AS userIds, " +
            "[(g)-[:GROUPADMIN]-(ad:User) | id(ad)] AS adminIds, " +
            "head([(g)--(n:News) | id(n)]) AS newsId, " +
            "[(g)-[:GROUPLIVETEST]-(lt:LiveTest) | id(lt)] AS liveTestIds " +
            "RETURN id(g) AS id, g.name AS name, g.code AS code, g.description AS description, " +
            "newsId, userIds, adminIds, liveTestIds")
    List<GroupDTO> getByManyIds(List<Long> groupIds);

    @Query("MATCH (u:User)-[gu:GROUPUSER]-(g:Group)" +
            " WHERE id(u) = $0 AND id(g) = $1" +
            " DELETE gu")
    void deleteUserFromGroup(Long userId, Long groupId);

    @Query("MATCH (u:User)-[ga:GROUPADMIN]-(g:Group)" +
            " WHERE id(u) = $0 AND id(g) = $1" +
            " DELETE ga")
    void deleteAdminFromGroup(Long userId, Long groupId);
}
