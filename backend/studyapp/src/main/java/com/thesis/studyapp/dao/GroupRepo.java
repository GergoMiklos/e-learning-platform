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

    @Depth(2)
    @Query("MATCH (g:Group)-[gr:GROUPUSER]->(u:User)" +
            " WHERE id(u)=$0" +
            " RETURN g")
    List<Group> findByUserId(Long userid);

    @Depth(2)
    @Query("MATCH (g:Group)-[gr:GROUPADMIN]->(u:User)" +
            " WHERE id(u)=$0" +
            " RETURN g")
    List<Group> findByAdminId(Long userid);
}
