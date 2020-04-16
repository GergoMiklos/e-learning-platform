package com.thesis.studyapp.dao;

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
    @Query("MATCH (u:User)-[:GROUPUSER]-(g:Group) OPTIONAL MATCH p = (:User)--(g:Group)--(:LiveTest)--(:Test) WHERE id(u)=$0 RETURN p")
    List<Group> findByUserId(Long userid);

    @Depth(2)
    @Query("MATCH (u:User)-[:GROUPADMIN]-(g:Group) OPTIONAL MATCH p = (:User)--(g:Group)--(:LiveTest) WHERE id(u)=$0 RETURN p")
    List<Group> findByAdminId(Long userid);
}
