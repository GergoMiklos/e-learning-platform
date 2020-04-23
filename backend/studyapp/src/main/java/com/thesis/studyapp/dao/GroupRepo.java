package com.thesis.studyapp.dao;

import com.thesis.studyapp.dto.GroupDTO;
import com.thesis.studyapp.model.Group;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepo extends Neo4jRepository<Group, Long> {

    List<Group> findByName(String name);

//MŰKÖDIK, de minden nested objectre null!!!
//    @Query("MATCH (:User)-[gr:GROUPUSER]-(g:Group)-[:GROUPUSER]-(u:User)" +
////            " WHERE id(u)=$0" +
////            " RETURN collect(g.id, g.name, collect(gr) as users")
    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group) WHERE id(u)=28 OPTIONAL MATCH (g:Group)-[:GROUPUSER]-(us:User) SET g.userIds = id(us) RETURN g")
    List<Group> findByUserId(Long userid);

    @Depth(2)
    @Query("MATCH (u:User)-[:GROUPADMIN]-(g:Group) OPTIONAL MATCH p = (g:Group)--() WHERE id(u)=$0 RETURN p")
    List<Group> findByAdminId(Long userid);

    @Query("MATCH (g:Group)-[:GROUPUSER]-(us:User) WHERE id(g)=26 RETURN id(g) as id, g.name as name, collect(id(us)) as userIds")
    GroupDTO findByDTOId(Long id);
}
