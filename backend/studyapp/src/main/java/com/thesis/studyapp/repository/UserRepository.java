package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.User;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long>, ObjectLoader<User> {
    String RETURN_USERDTO = " RETURN id(u) AS id, u.name AS name, u.code AS code";
    String ORDER_BY_NAME = " ORDER BY u.name";

    Optional<User> findByCodeIgnoreCase(String code, @Depth int depth);

    Optional<User> findByAuthDataUsernameIgnoreCase(String username, @Depth int depth);

    List<User> findByIdIn(List<Long> ids, @Depth int depth);

    List<User> findByStudentGroupsIdOrderByName(Long studentGroupId, @Depth int depth);

    List<User> findByTeacherGroupsIdOrderByName(Long teacherGroupId, @Depth int depth);

    List<User> findByParentsIdOrderByName(Long parentId, @Depth int depth);

    boolean existsByAuthDataUsernameIgnoreCase(String username);

    boolean existsByCodeIgnoreCase(String code);

    @Query("MATCH (parent:User)-[sp:STUDENTPARENT]->(student:User)" +
            " WHERE id(parent) = $parentId AND id(student) = $studentId" +
            " DELETE sp")
    void deleteFollowedStudent(@Param("parentId") Long parentId, @Param("studentId") Long studentId);


}
