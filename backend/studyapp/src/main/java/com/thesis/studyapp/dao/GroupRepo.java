package com.thesis.studyapp.dao;

import com.thesis.studyapp.model.Group;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepo extends CrudRepository<Group, Long> {

    Group findByName(String name);

//MŰKÖDIK!!!
    @Query("MATCH (g:Group)-[:GROUPUSER]->(u:User {userName:$0}) return g")
    List<Group> findByUserName(String name);
}
