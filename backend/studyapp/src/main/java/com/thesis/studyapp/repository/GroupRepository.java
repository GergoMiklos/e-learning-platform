package com.thesis.studyapp.repository;

import com.thesis.studyapp.model.Group;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends Neo4jRepository<Group, Long>, ObjectLoader<Group> {

    Optional<Group> findByCodeIgnoreCase(String name, @Depth int depth);

    List<Group> findByIdIn(List<Long> groupIds, @Depth int depth);

    List<Group> findByStudentsIdOrderByName(Long studentId, @Depth int depth);

    List<Group> findByTeachersIdOrderByName(Long teacherId, @Depth int depth);

    boolean existsByCodeIgnoreCase(String code);

    @Query("MATCH (student:User)-[gs:GROUPSTUDENT]-(g:Group)" +
            " WHERE id(student) = $studentId AND id(g) = $groupId" +
            " DELETE gs")
    void deleteStudent(@Param("groupId") Long groupId, @Param("studentId") Long studentId);

    @Query("MATCH (teacher:User)-[gt:GROUPTEACHER]-(g:Group)" +
            " WHERE id(teacher) = $teacherId AND id(g) = $groupId" +
            " DELETE gt")
    void deleteTeacher(@Param("groupId") Long groupId, @Param("teacherId") Long teacherId);
}
