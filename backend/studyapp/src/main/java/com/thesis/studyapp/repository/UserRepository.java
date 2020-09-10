package com.thesis.studyapp.repository;

import com.thesis.studyapp.dto.UserDto;
import com.thesis.studyapp.model.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
    String RETURN_USERDTO = " RETURN id(u) AS id, u.name AS name, u.code AS code";
    String ORDER_BY_NAME = " ORDER BY u.name";

    Optional<User> findByCode(String name);

    @Query("MATCH (u:User) WHERE id(u) IN $0" + RETURN_USERDTO)
    List<UserDto> getByIds(List<Long> ids);

    @Query("MATCH (u:User) WHERE id(u) = $0" + RETURN_USERDTO)
    Optional<UserDto> getById(Long id);

    @Query("MATCH (u:User)-[:GROUPSTUDENT]-(g:Group) WHERE id(g) = $0" + RETURN_USERDTO + ORDER_BY_NAME)
    List<UserDto> getStudentByGroupId(Long groupId);

    @Query("MATCH (u:User)-[:GROUPTEACHER]-(g:Group) WHERE id(g) = $0" + RETURN_USERDTO + ORDER_BY_NAME)
    List<UserDto> getTeacherByGroupId(Long groupId);

    @Query("MATCH (u:User)<-[:STUDENTPARENT]-(parent:User) WHERE id(parent) = $0" + RETURN_USERDTO + ORDER_BY_NAME)
    List<UserDto> getFollowedStudents(Long id);


}
