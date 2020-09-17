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
public interface UserRepository extends Neo4jRepository<User, Long> {
    String RETURN_USERDTO = " RETURN id(u) AS id, u.name AS name, u.code AS code";
    String ORDER_BY_NAME = " ORDER BY u.name";

    Optional<User> findByCode(String code, @Depth int depth);

    List<User> findByIdIn(List<Long> ids,
                          @Depth
                                  int depth);

    List<User> findByStudentGroupsIdOrderByName(Long studentGroupId, @Depth int depth);

    List<User> findByTeacherGroupsIdOrderByName(Long teacherGroupId, @Depth int depth);

    List<User> findByParentsIdOrderByName(Long parentId, @Depth int depth);

    @Query("MATCH (parent:User)-[sp:STUDENTPARENT]->(student:Student)" +
            " WHERE id(parent) = $parentId AND id(student) = $studentId" +
            " DELETE sp")
    void deleteFollowedStudent(@Param("parentId") Long parentId, @Param("studentId") Long studentId);


//    @Query("MATCH (u:User) WHERE id(u) IN $0" + RETURN_USERDTO)
//    List<UserDto> getByIds(List<Long> ids);
//
//    @Query("MATCH (u:User) WHERE id(u) = $0" + RETURN_USERDTO)
//    Optional<UserDto> getById(Long id);
//
//    @Query("MATCH (u:User)-[:GROUPSTUDENT]-(g:Group) WHERE id(g) = $0" + RETURN_USERDTO + ORDER_BY_NAME)
//    List<UserDto> getStudentByGroupId(Long groupId);
//
//    @Query("MATCH (u:User)-[:GROUPTEACHER]-(g:Group) WHERE id(g) = $0" + RETURN_USERDTO + ORDER_BY_NAME)
//    List<UserDto> getTeacherByGroupId(Long groupId);
//
//    @Query("MATCH (u:User)<-[:STUDENTPARENT]-(parent:User) WHERE id(parent) = $0" + RETURN_USERDTO + ORDER_BY_NAME)
//    List<UserDto> getFollowedStudents(Long id);


}
