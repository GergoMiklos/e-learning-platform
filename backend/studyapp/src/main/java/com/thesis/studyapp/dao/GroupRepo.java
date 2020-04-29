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

    List<Group> findByName(String name);

//TODO nem kell mindig minden id
    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group) WHERE id(u) = $0 " +
            "WITH g, " +
            "[(g)-[:GROUPUSER]-(us:User) | id(us)] AS userIds, " +
            "[(g)-[:GROUPADMIN]-(ad:User) | id(ad)] AS adminIds, " +
            "[(g)--(n:News) | id(n)] AS newsIds, " +
            "[(g)-[:GROUPLIVETEST]-(lt:LiveTest) | id(lt)] AS liveTestIds " +
            "RETURN id(g) as id, g.name as name, userIds, adminIds, newsIds, liveTestIds")
    List<GroupDTO> findByUserId(Long userId);

    @Query("MATCH (u:User)-[:GROUPADMIN]-(g:Group) WHERE id(u) = $0 " +
            "WITH g, " +
            "[(g)-[:GROUPUSER]-(us:User) | id(us)] AS userIds, " +
            "[(g)-[:GROUPADMIN]-(ad:User) | id(ad)] AS adminIds, " +
            "[(g)--(n:News) | id(n)] AS newsIds, " +
            "[(g)-[:GROUPLIVETEST]-(lt:LiveTest) | id(lt)] AS liveTestIds " +
            "RETURN id(g) as id, g.name as name, userIds, adminIds, newsIds, liveTestIds")
    List<GroupDTO> findByAdminId(Long userId);

    //ELÉG CSAK EZ + IN
    @Query("MATCH (g:Group) WHERE id(g) = $0 " +
            "WITH g, " +
            "[(g)-[:GROUPUSER]-(us:User) | id(us)] AS userIds, " +
            "[(g)-[:GROUPADMIN]-(ad:User) | id(ad)] AS adminIds, " +
            "[(g)--(n:News) | id(n)] AS newsIds, " +
            "[(g)-[:GROUPLIVETEST]-(lt:LiveTest) | id(lt)] AS liveTestIds " +
            "RETURN id(g) as id, g.name as name, userIds, adminIds, newsIds, liveTestIds")
    Optional<GroupDTO> findByGroupId(Long groupId);



    @Depth(2)
    @Query("MATCH (u:User)-[:GROUPADMIN]-(g:Group) OPTIONAL MATCH p = (g:Group)--() WHERE id(u)=$0 RETURN p")
    List<Group> findByAdminId2(Long userid);

    @Query("MATCH (g:Group)-[:GROUPUSER]-(us:User) WHERE id(g)=26 RETURN id(g) as id, g.name as name, collect(id(us)) as userIds")
    GroupDTO findByDTOId(Long id);

    @Query("MATCH (g:Group) WHERE id(g) IN $0 " +
            "WITH g, " +
            "[(g)-[:GROUPUSER]-(us:User) | id(us)] AS userIds, " +
            "[(g)-[:GROUPADMIN]-(ad:User) | id(ad)] AS adminIds, " +
            "[(g)--(n:News) | id(n)] AS newsIds, " +
            "[(g)-[:GROUPLIVETEST]-(lt:LiveTest) | id(lt)] AS liveTestIds " +
            "RETURN id(g) as id, g.name as name, userIds, adminIds, newsIds, liveTestIds")
    List<GroupDTO> findByManyIds(List<Long> groupIds);
}
